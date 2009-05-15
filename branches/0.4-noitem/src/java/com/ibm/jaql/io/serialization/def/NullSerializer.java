package com.ibm.jaql.io.serialization.def;

import java.io.DataInput;
import java.io.DataOutput;
import java.io.IOException;

import com.ibm.jaql.io.serialization.BasicSerializer;
import com.ibm.jaql.json.type.JsonValue;


@SuppressWarnings("unchecked")
public class NullSerializer extends BasicSerializer
{
  @Override
  public JsonValue newInstance()
  {
    return null;
  }
  
  @Override
  public int compare(DataInput in1, DataInput in2) throws IOException
  {
    return 0;
  }

  @Override
  public void copy(DataInput in, DataOutput out) throws IOException
  {
  }

  @Override
  public JsonValue read(DataInput in, JsonValue target) throws IOException
  {
    return null;
  }

  @Override
  public void skip(DataInput in) throws IOException
  {
  }

  @Override
  public void write(DataOutput out, JsonValue target) throws IOException
  {
  }
}
