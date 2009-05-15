/*
 * Copyright (C) IBM Corp. 2008.
 * 
 * Licensed under the Apache License, Version 2.0 (the "License"); you may not
 * use this file except in compliance with the License. You may obtain a copy of
 * the License at
 * 
 * http://www.apache.org/licenses/LICENSE-2.0
 * 
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS, WITHOUT
 * WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied. See the
 * License for the specific language governing permissions and limitations under
 * the License.
 */
package com.ibm.jaql.io.hadoop.converter;

import org.apache.hadoop.io.Writable;
import org.apache.hadoop.io.WritableComparable;
import org.apache.nutch.metadata.Metadata;
import org.apache.nutch.net.protocols.Response;
import org.apache.nutch.protocol.Content;

import com.ibm.jaql.io.converter.ToJson;
import com.ibm.jaql.json.type.JsonBinary;
import com.ibm.jaql.json.type.JsonString;
import com.ibm.jaql.json.type.BufferedJsonRecord;
import com.ibm.jaql.json.type.JsonValue;
import com.ibm.jaql.json.type.JsonType;

public class FromNutchContent extends HadoopRecordToJson<WritableComparable<?>, Writable> {

  public static enum Field {
    URL("url"),
    BASEURL("bUrl"),
    TYPE("type"),
    CONTENT("cont"),
    META("meta");
    
    public final String name;
    
    Field(String n) {
      this.name = n;
    }
  };
  
  @Override
  protected ToJson<WritableComparable<?>> createKeyConverter() {
    // TODO Auto-generated method stub
    return null;
  }

  // TODO: the generic should be any subclass of Writable
  @Override
  protected ToJson<Writable> createValueConverter() {
    return new ToJson<Writable> () {
      
      private JsonString sVal = new JsonString();
      private JsonBinary bVal = new JsonBinary();
      
      public JsonValue convert(Writable src, JsonValue tgt)
      {
        // expect src to be Content
        Content c = (Content)src;
        
        // expect tgt's value to be MemoryJRecord 
        BufferedJsonRecord r = (BufferedJsonRecord)tgt;
        
        // the metadata
        Metadata meta = c.getMetadata();
        
        // set the fixed fields
        ((JsonString)r.getValue(Field.URL.name)).setCopy(c.getUrl().getBytes());
        ((JsonString)r.getValue(Field.BASEURL.name)).setCopy(c.getBaseUrl().getBytes());
        ((JsonString)r.getValue(Field.TYPE.name)).setCopy(c.getContentType().getBytes());
        
        String cTypeFromMeta = meta.get(Response.CONTENT_TYPE);
        String cTypeFromTop  = c.getContentType();
        JsonValue cValue = r.getValue(Field.CONTENT.name);
        if( (cTypeFromMeta != null && cTypeFromMeta.indexOf("text") >= 0 ) ||
            (cTypeFromTop  != null && cTypeFromTop.indexOf("text") >= 0 ) ){
          sVal.setCopy(c.getContent());
          if(cValue.getEncoding().getType() != JsonType.STRING) 
            cValue = sVal;
        } else {
          bVal.setBytes(c.getContent());
          if(cValue.getEncoding().getType() != JsonType.BINARY)
            cValue = bVal;
        }
        
        // set the dynamic metadata
        BufferedJsonRecord mr = (BufferedJsonRecord)r.getValue(Field.META.name);
        mr.clear();
        String[] names = meta.names();
        for(int i = 0; i < names.length; i++) {
          mr.add(names[i], new JsonString(meta.get(names[i])));
        }
        
        return r;
      }
      
      public JsonValue createInitialTarget()
      {
        int n = Field.values().length;
        BufferedJsonRecord r = new BufferedJsonRecord(n);
        r.add(Field.URL.name, new JsonString());
        r.add(Field.BASEURL.name, new JsonString());
        r.add(Field.TYPE.name, new JsonString());
        r.add(Field.CONTENT.name, sVal);
        r.add(Field.META.name, new BufferedJsonRecord());
        
        return r;
      }
    };
  }
  
}