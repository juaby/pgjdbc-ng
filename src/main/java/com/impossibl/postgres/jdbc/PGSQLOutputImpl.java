/**
 * Copyright (c) 2013, impossibl.com
 * All rights reserved.
 *
 * Redistribution and use in source and binary forms, with or without
 * modification, are permitted provided that the following conditions are met:
 *
 *  * Redistributions of source code must retain the above copyright notice,
 *    this list of conditions and the following disclaimer.
 *  * Redistributions in binary form must reproduce the above copyright
 *    notice, this list of conditions and the following disclaimer in the
 *    documentation and/or other materials provided with the distribution.
 *  * Neither the name of impossibl.com nor the names of its contributors may
 *    be used to endorse or promote products derived from this software
 *    without specific prior written permission.
 *
 * THIS SOFTWARE IS PROVIDED BY THE COPYRIGHT HOLDERS AND CONTRIBUTORS "AS IS"
 * AND ANY EXPRESS OR IMPLIED WARRANTIES, INCLUDING, BUT NOT LIMITED TO, THE
 * IMPLIED WARRANTIES OF MERCHANTABILITY AND FITNESS FOR A PARTICULAR PURPOSE
 * ARE DISCLAIMED. IN NO EVENT SHALL THE COPYRIGHT OWNER OR CONTRIBUTORS BE
 * LIABLE FOR ANY DIRECT, INDIRECT, INCIDENTAL, SPECIAL, EXEMPLARY, OR
 * CONSEQUENTIAL DAMAGES (INCLUDING, BUT NOT LIMITED TO, PROCUREMENT OF
 * SUBSTITUTE GOODS OR SERVICES; LOSS OF USE, DATA, OR PROFITS; OR BUSINESS
 * INTERRUPTION) HOWEVER CAUSED AND ON ANY THEORY OF LIABILITY, WHETHER IN
 * CONTRACT, STRICT LIABILITY, OR TORT (INCLUDING NEGLIGENCE OR OTHERWISE)
 * ARISING IN ANY WAY OUT OF THE USE OF THIS SOFTWARE, EVEN IF ADVISED OF THE
 * POSSIBILITY OF SUCH DAMAGE.
 */
package com.impossibl.postgres.jdbc;

import com.impossibl.postgres.api.jdbc.PGSQLOutput;
import com.impossibl.postgres.types.CompositeType;
import com.impossibl.postgres.types.CompositeType.Attribute;
import com.impossibl.postgres.utils.guava.ByteStreams;
import com.impossibl.postgres.utils.guava.CharStreams;

import static com.impossibl.postgres.jdbc.Exceptions.NOT_IMPLEMENTED;
import static com.impossibl.postgres.jdbc.Exceptions.NOT_SUPPORTED;
import static com.impossibl.postgres.jdbc.SQLTypeUtils.coerce;
import static com.impossibl.postgres.jdbc.SQLTypeUtils.mapSetType;

import java.io.IOException;
import java.io.InputStream;
import java.io.Reader;
import java.math.BigDecimal;
import java.net.URL;
import java.nio.charset.StandardCharsets;
import java.sql.Array;
import java.sql.Blob;
import java.sql.Clob;
import java.sql.Date;
import java.sql.NClob;
import java.sql.Ref;
import java.sql.RowId;
import java.sql.SQLData;
import java.sql.SQLException;
import java.sql.SQLType;
import java.sql.SQLXML;
import java.sql.Struct;
import java.sql.Time;
import java.sql.Timestamp;
import java.util.Collections;



public class PGSQLOutputImpl implements PGSQLOutput {

  private PGConnectionImpl connection;
  private CompositeType type;
  private int currentAttributeIdx;
  private Object[] attributeValues;

  public PGSQLOutputImpl(PGConnectionImpl connection, CompositeType type) {
    this.connection = connection;
    this.type = type;
    this.attributeValues = new Object[type.getAttributes().size()];
  }

  public Object[] getAttributeValues() {
    return attributeValues;
  }

  void writeNextAttributeValue(Object val) throws SQLException {

    Attribute attr = type.getAttribute(currentAttributeIdx + 1);
    if (attr == null) {
      throw new SQLException("invalid attribute access");
    }

    Class<?> targetType = mapSetType(attr.getType());

    attributeValues[currentAttributeIdx++] = coerce(val, attr.getType(), targetType, Collections.<String, Class<?>>emptyMap(), connection);
  }

  @Override
  public void writeString(String x) throws SQLException {
    writeNextAttributeValue(x);
  }

  @Override
  public void writeBoolean(boolean x) throws SQLException {
    writeNextAttributeValue(x);
  }

  @Override
  public void writeByte(byte x) throws SQLException {
    writeNextAttributeValue(x);
  }

  @Override
  public void writeShort(short x) throws SQLException {
    writeNextAttributeValue(x);
  }

  @Override
  public void writeInt(int x) throws SQLException {
    writeNextAttributeValue(x);
  }

  @Override
  public void writeLong(long x) throws SQLException {
    writeNextAttributeValue(x);
  }

  @Override
  public void writeFloat(float x) throws SQLException {
    writeNextAttributeValue(x);
  }

  @Override
  public void writeDouble(double x) throws SQLException {
    writeNextAttributeValue(x);
  }

  @Override
  public void writeBigDecimal(BigDecimal x) throws SQLException {
    writeNextAttributeValue(x);
  }

  @Override
  public void writeBytes(byte[] x) throws SQLException {
    writeNextAttributeValue(x);
  }

  @Override
  public void writeDate(Date x) throws SQLException {
    writeNextAttributeValue(x);
  }

  @Override
  public void writeTime(Time x) throws SQLException {
    writeNextAttributeValue(x);
  }

  @Override
  public void writeTimestamp(Timestamp x) throws SQLException {
    writeNextAttributeValue(x);
  }

  @Override
  public void writeCharacterStream(Reader x) throws SQLException {
    try {
      writeNextAttributeValue(CharStreams.toString(x));
    }
    catch (IOException e) {
      throw new SQLException(e);
    }
  }

  @Override
  public void writeAsciiStream(InputStream x) throws SQLException {
    try {
      writeNextAttributeValue(new String(ByteStreams.toByteArray(x), StandardCharsets.US_ASCII));
    }
    catch (IOException e) {
      throw new SQLException(e);
    }
  }

  @Override
  public void writeBinaryStream(InputStream x) throws SQLException {
    try {
      writeNextAttributeValue(ByteStreams.toByteArray(x));
    }
    catch (IOException e) {
      throw new SQLException(e);
    }
  }

  @Override
  public void writeArray(Array x) throws SQLException {
    writeNextAttributeValue(x);
  }

  @Override
  public void writeURL(URL x) throws SQLException {
    writeNextAttributeValue(x);
  }

  @Override
  public void writeObject(SQLData x) throws SQLException {
    writeNextAttributeValue(x);
  }

  @Override
  public void writeObject(Object x) throws SQLException {
    writeNextAttributeValue(x);
  }

  @Override
  public void writeBlob(Blob x) throws SQLException {
    writeNextAttributeValue(x);
  }

  @Override
  public void writeClob(Clob x) throws SQLException {
    writeNextAttributeValue(x);
  }

  @Override
  public void writeStruct(Struct x) throws SQLException {
    writeNextAttributeValue(x);
  }

  @Override
  public void writeSQLXML(SQLXML x) throws SQLException {
    writeNextAttributeValue(x);
  }

  @Override
  public void writeRowId(RowId x) throws SQLException {
    writeNextAttributeValue(x);
  }

  @Override
  public void writeRef(Ref x) throws SQLException {
    throw NOT_IMPLEMENTED;
  }

  @Override
  public void writeNString(String x) throws SQLException {
    throw NOT_SUPPORTED;
  }

  @Override
  public void writeNClob(NClob x) throws SQLException {
    throw NOT_SUPPORTED;
  }

  /**
   * {@inheritDoc}
   */
  @Override
  public void writeObject(Object x, SQLType targetSqlType) throws SQLException {
    throw NOT_IMPLEMENTED;
  }
}
