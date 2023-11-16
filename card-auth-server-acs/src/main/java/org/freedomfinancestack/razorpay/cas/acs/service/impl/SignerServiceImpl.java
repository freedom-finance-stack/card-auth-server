package org.freedomfinancestack.razorpay.cas.acs.service.impl;

import java.nio.charset.StandardCharsets;
import java.security.KeyPair;
import java.security.interfaces.ECPrivateKey;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import javax.crypto.SecretKey;

import org.freedomfinancestack.razorpay.cas.acs.dto.SignedContent;
import org.freedomfinancestack.razorpay.cas.acs.gateway.ClientType;
import org.freedomfinancestack.razorpay.cas.acs.gateway.config.GatewayConfig;
import org.freedomfinancestack.razorpay.cas.acs.service.SignerService;
import org.freedomfinancestack.razorpay.cas.acs.utils.HexDump;
import org.freedomfinancestack.razorpay.cas.acs.utils.SecurityUtil;
import org.freedomfinancestack.razorpay.cas.contract.AREQ;
import org.freedomfinancestack.razorpay.cas.contract.EphemPubKey;
import org.freedomfinancestack.razorpay.cas.dao.model.SignerDetail;
import org.freedomfinancestack.razorpay.cas.dao.model.SignerDetailPK;
import org.freedomfinancestack.razorpay.cas.dao.model.Transaction;
import org.freedomfinancestack.razorpay.cas.dao.repository.SignerDetailRepository;
import org.nimbusds.jose.crypto.CustomConcatKDF;
import org.nimbusds.jose.crypto.CustomECDH;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.nimbusds.jose.jwk.ECKey;
import com.nimbusds.jose.jwk.JWK;
import com.nimbusds.jose.util.Base64;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@Service
@Slf4j
@RequiredArgsConstructor(onConstructor = @__(@Autowired))
public class SignerServiceImpl implements SignerService {
    private final SignerDetailRepository signerDetailRepository;
    private final GatewayConfig gatewayConfig;

    @Transactional
    @Override
    public String getAcsSignedContent(AREQ areq, Transaction transaction, String acsUrl) {
        SignerDetail signerDetail = null;
        String signedData = null;

        try {
            // 116
            // step 1 : Generates a fresh ephemeral key pair (QT, dT) as described in Annex C and
            // makes QT available for inclusion in the ARes message.
            // Step 1 - Create Ephermal Keyair
            KeyPair acsKeyPair = SecurityUtil.generateEphermalKeyPair();

            // step 2 : Checks that QC is a point on the curve P-256.
            // Step 2 - Create JWK with Public key info (Nimbus Jose)
            JWK acsPublicKeyJWK = SecurityUtil.getPublicKey(acsKeyPair);

            // step 3 : Completes the Diffie-Hellman key exchange process as a local mechanism
            // according to JWA (RFC 7518) in Direct Key Agreement mode using curve P-256, dT and QC
            // to produce a pair of CEKs (one for each direction) which are identified by the ACS
            // Transaction ID.
            // Step 3 -  Generate ECPublicKey from JWK (Nimbus Jose)
            ECKey sdkPubKey = SecurityUtil.getECKey(areq.getSdkEphemPubKey());

            // Generate shared key
            generateSHA256SecretKey(areq, transaction, sdkPubKey, acsKeyPair);

            EphemPubKey acsPublicKey = new EphemPubKey();
            for (Map.Entry entry : acsPublicKeyJWK.getRequiredParams().entrySet()) {
                String key = entry.getKey().toString();
                String value = entry.getValue().toString();

                if (key.equals("crv")) {
                    acsPublicKey.setCrv(value);
                } else if (key.equals("kty")) {
                    acsPublicKey.setKty(value);
                } else if (key.equals("x")) {
                    acsPublicKey.setX(value);
                } else if (key.equals("y")) {
                    acsPublicKey.setY(value);
                }
            }

            SignedContent signedContent = new SignedContent();
            signedContent.setAcsEphemPubKey(acsPublicKey);
            signedContent.setSdkEphemPubKey(areq.getSdkEphemPubKey());
            signedContent.setAcsURL(acsUrl);
            Gson gson = new GsonBuilder().disableHtmlEscaping().create();
            String signedJsonObject = gson.toJson(signedContent);

            Byte networkCode = transaction.getTransactionCardDetail().getNetworkCode();
            Optional<SignerDetail> signerDetailOptional =
                    signerDetailRepository.findById(
                            new SignerDetailPK(transaction.getInstitutionId(), networkCode));

            if (signerDetailOptional.isPresent()) signerDetail = signerDetailOptional.get();

            List<Base64> x509CertChain =
                    SecurityUtil.getKeyInfo(
                            signerDetail,
                            "/opt/card-auth-server/cas-acs/bin/config/clientCertificate1910.crt");

            KeyPair keyPair = SecurityUtil.getRSAKeyPairFromKeystore(x509CertChain);

            signedData =
                    SecurityUtil.generateDigitalSignatureWithPS256(
                            keyPair, x509CertChain, signedJsonObject);

        } catch (Exception e) {
            e.printStackTrace();
        }
        return signedData;
    }

    private void generateSHA256SecretKey(
            AREQ areq, Transaction transaction, ECKey sdkPubKey, KeyPair acsKeyPair)
            throws Exception {

        // Step 4 - Perform KeyAgreement and derive SecretKey
        SecretKey Z =
                CustomECDH.deriveSharedSecret(
                        sdkPubKey.toECPublicKey(), (ECPrivateKey) acsKeyPair.getPrivate(), null);

        CustomConcatKDF concatKDF = new CustomConcatKDF("SHA-256");

        String algIdString = "";
        String partyVInfoString = areq.getSdkReferenceNumber();
        int keylength = 256; // A128CBC-HS256

        byte[] algID =
                CustomConcatKDF.encodeDataWithLength(algIdString.getBytes(StandardCharsets.UTF_8));
        byte[] partyUInfo = CustomConcatKDF.encodeDataWithLength(new byte[0]);
        byte[] partyVInfo =
                CustomConcatKDF.encodeDataWithLength(
                        partyVInfoString.getBytes(StandardCharsets.UTF_8));
        byte[] suppPubInfo = CustomConcatKDF.encodeIntData(keylength);
        byte[] suppPrivInfo = CustomConcatKDF.encodeNoData();

        SecretKey derivedKey =
                concatKDF.deriveKey(
                        Z, keylength, algID, partyUInfo, partyVInfo, suppPubInfo, suppPrivInfo);

        transaction
                .getTransactionSdkDetail()
                .setAcsSecretKey(HexDump.byteArrayToHex(derivedKey.getEncoded()));
    }
}
