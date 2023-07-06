package com.razorpay.threeds.utils;

import java.text.ParseException;

import org.junit.jupiter.api.Test;

import com.razorpay.threeds.dto.CardDetailDto;

import static org.junit.jupiter.api.Assertions.*;

class UtilTests {

  @Test
  public void testIsNullorBlankTrue() {
    // write junit test cases for isNullorBlank method
    assertTrue(Util.isNullorBlank(null));
    assertTrue(Util.isNullorBlank(""));
  }

  @Test
  public void testIsNullorBlankFalse() {
    assertFalse(Util.isNullorBlank("test"));
    assertFalse(Util.isNullorBlank(new CardDetailDto()));
  }

  @Test
  public void testGenerateUUID() {
    assertTrue(Util.generateUUID().length() > 0);
  }

  @Test
  public void testGetTimeStampFromString() throws Exception {
    assertEquals(
        "2023-12-12 12:01:01.0",
        Util.getTimeStampFromString("20231212120101", "yyyyMMddHHmmss").toString());
    assertEquals(
        "2012-12-12 00:00:00.0",
        Util.getTimeStampFromString("12.12.2012", "dd.MM.yyyy").toString());
  }

  @Test
  public void testGetTimeStampFromStringFail() throws Exception {
    assertThrows(
        ParseException.class, () -> Util.getTimeStampFromString("12122023", "yyyyMMddHHmmss"));
    assertThrows(ParseException.class, () -> Util.getTimeStampFromString("121223", "dd.MM.yyyy"));
  }
}
