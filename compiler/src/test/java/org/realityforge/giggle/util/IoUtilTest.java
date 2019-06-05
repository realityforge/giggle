package org.realityforge.giggle.util;

import gir.io.FileUtil;
import java.nio.file.Path;
import org.realityforge.giggle.AbstractTest;
import org.testng.annotations.Test;
import static org.testng.Assert.*;

public class IoUtilTest
  extends AbstractTest
{
  @Test
  public void deleteDirIfExists_dirNoExist()
    throws Exception
  {
    inIsolatedDirectory( () -> {
      final Path dir = FileUtil.getCurrentDirectory().resolve( "somedir" );

      assertFalse( dir.toFile().exists() );

      IoUtil.deleteDirIfExists( dir );

      assertFalse( dir.toFile().exists() );
    } );
  }

  @Test
  public void deleteDirIfExists_dirExistsAndIsNonEmpty()
    throws Exception
  {
    inIsolatedDirectory( () -> {
      final Path dir = FileUtil.createLocalTempDir();
      FileUtil.write( "file.txt", "" );
      assertTrue( dir.resolve( "subdir" ).toFile().mkdir() );
      FileUtil.write( "subdir/file.txt", "" );

      assertTrue( dir.toFile().exists() );

      IoUtil.deleteDirIfExists( dir );

      assertFalse( dir.toFile().exists() );
    } );
  }

}
