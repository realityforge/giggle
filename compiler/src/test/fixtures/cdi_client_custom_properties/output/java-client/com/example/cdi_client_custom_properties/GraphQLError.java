package com.example.cdi_client_custom_properties;

import java.util.Map;
import java.util.Objects;
import javax.annotation.Generated;
import javax.annotation.Nonnull;
import javax.annotation.Nullable;

@Generated("org.realityforge.giggle.Main")
public final class GraphQLError {
  private String message;

  @Nullable
  private Object[] path;

  @Nullable
  private Location[] locations;

  @Nullable
  private Map<String, Object> extensions;

  @Nonnull
  public String getMessage() {
    return Objects.requireNonNull( message );
  }

  public void setMessage(@Nonnull final String message) {
    this.message = Objects.requireNonNull( message );
  }

  @Nullable
  public Object[] getPath() {
    return path;
  }

  public void setPath(@Nullable final Object[] path) {
    this.path = path;
  }

  @Nullable
  public Location[] getLocations() {
    return locations;
  }

  public void setLocations(@Nullable final Location[] locations) {
    this.locations = locations;
  }

  @Nullable
  public Map<String, Object> getExtensions() {
    return extensions;
  }

  public void setExtensions(@Nullable final Map<String, Object> extensions) {
    this.extensions = extensions;
  }

  public static final class Location {
    private int line;

    private int column;

    public int getLine() {
      return line;
    }

    public void setLine(final int line) {
      this.line = line;
    }

    public int getColumn() {
      return column;
    }

    public void setColumn(final int column) {
      this.column = column;
    }
  }
}
