package org.realityforge.giggle;

import java.util.ArrayList;
import java.util.logging.Handler;
import java.util.logging.LogRecord;
import java.util.stream.Collectors;

class TestHandler
  extends Handler
{
  private ArrayList<LogRecord> _records = new ArrayList<>();

  @Override
  public void publish( final LogRecord record )
  {
    _records.add( record );
  }

  ArrayList<LogRecord> getRecords()
  {
    return _records;
  }

  @Override
  public void flush()
  {
  }

  @Override
  public void close()
    throws SecurityException
  {
  }

  @Override
  public String toString()
  {
    return getRecords().stream().map( LogRecord::getMessage ).collect( Collectors.joining( "\n" ) );
  }
}
