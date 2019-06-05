package org.realityforge.giggle;

final class ExitCodes
{
  static final int SUCCESS_EXIT_CODE = 0;
  static final int ERROR_EXIT_CODE = 1;
  static final int ERROR_PARSING_ARGS_EXIT_CODE = 2;
  static final int ERROR_READING_SCHEMA_EXIT_CODE = 3;
  static final int ERROR_PARSING_SCHEMA_EXIT_CODE = 4;
  static final int ERROR_READING_DOCUMENT_EXIT_CODE = 5;
  static final int ERROR_PARSING_DOCUMENT_EXIT_CODE = 6;
  static final int UNKNOWN_GENERATOR_EXIT_CODE = 7;
  static final int BAD_TYPE_MAPPING_EXIT_CODE = 8;

  private ExitCodes()
  {
  }
}
