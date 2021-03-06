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
package com.ibm.jaql.lang.expr.core;

import java.util.Enumeration;
import java.util.HashSet;

import com.ibm.jaql.json.type.BufferedJsonRecord;
import com.ibm.jaql.json.type.JsonString;
import com.ibm.jaql.json.type.JsonValue;
import com.ibm.jaql.lang.core.Context;
import com.ibm.jaql.lang.core.Var;
import com.ibm.jaql.lang.core.VarMap;
import com.ibm.jaql.lang.expr.metadata.MappingTable;
import com.ibm.jaql.lang.expr.path.PathExpr;
import com.ibm.jaql.lang.expr.path.PathFieldValue;
import com.ibm.jaql.lang.expr.path.PathStep;
import com.ibm.jaql.util.Bool3;
import com.ibm.jaql.util.FastPrinter;

/**
 * 
 */
//e.g., a: 1
public class NameValueBinding extends FieldExpr
{

  boolean required;

  /**
   * { exprs[0] : exprs[1] } required=true { exprs[0]?: exprs[1] }
   * required=false
   * 
   * @param required
   * @param exprs
   */
  public NameValueBinding(boolean required, Expr[] exprs)
  {
    super(exprs);
    this.required = required;
  }

  /**
   * @param nameExpr
   * @param valueExpr
   * @param required
   */
  public NameValueBinding(Expr nameExpr, Expr valueExpr, boolean required)
  {
    this(required, new Expr[]{nameExpr, valueExpr});
  }

  /**
   * @param name
   * @param value
   * @param required
   */
  public NameValueBinding(String name, Expr value, boolean required)
  {
    this(new JsonString(name), value, required);
  }

  /**
   * @param name
   * @param value
   * @param required
   */
  public NameValueBinding(JsonString name, Expr value, boolean required)
  {
    this(required, new Expr[]{new ConstExpr(name), value});
  }
  /**
   * @param name
   * @param value
   */
  public NameValueBinding(Expr name, Expr value)
  {
    this(name, value, true);
  }

  /**
   * @param name
   * @param value
   */
  public NameValueBinding(String name, Expr value)
  {
    this(name, value, true);
  }
  
  /**
   * @param name
   * @param value
   */
  public NameValueBinding(JsonString name, Expr value)
  {
    this(name, value, true);
  }
  
  /**
   * 
   * @param var var name is name, var value is value
   * @param required
   */
  public NameValueBinding(Var var, boolean required)
  {
    this(fieldName(var), new VarExpr(var), required);
  }

  /**
   * Return the var name as a field name.  
   * It strings any leading $ from the name for backwards compatiblity.  
   */
  public static String fieldName(Var var)
  {
    String name = var.name();
    if( name.length() > 0 && name.charAt(0) == '$' )
    {
      return name.substring(1);
    }
    return name;
  }

  /*
   * (non-Javadoc)
   * 
   * @see com.ibm.jaql.lang.expr.core.Expr#clone(com.ibm.jaql.lang.core.VarMap)
   */
  public NameValueBinding clone(VarMap varMap)
  {
    return new NameValueBinding(required, cloneChildren(varMap));
  }

  public JsonString staticName()
  {
    if (exprs[0] instanceof ConstExpr)
    {
      ConstExpr c = (ConstExpr) exprs[0];
      return (JsonString) c.value;
    }
    return null;
  }

  /*
   * (non-Javadoc)
   * 
   * @see com.ibm.jaql.lang.expr.core.FieldExpr#staticNameMatches(com.ibm.jaql.json.type.JString)
   */
  public Bool3 staticNameMatches(JsonString name)
  {
    if (exprs[0] instanceof ConstExpr)
    {
      ConstExpr c = (ConstExpr) exprs[0];
      JsonString str = (JsonString) c.value;
      if (str != null && str.equals(name))
      {
        return Bool3.TRUE;
      }
      return Bool3.FALSE;
    }
    return Bool3.UNKNOWN;
  }

  /**
   * @return
   */
  public Expr nameExpr()
  {
    return exprs[0];
  }

  /**
   * @return
   */
  public Expr valueExpr()
  {
    return exprs[1];
  }

  /** true if the field is always constructed; false if constructed only when nonnull */
  public boolean isRequired()
  {
    return required;
  }

  /** true if the field is always constructed; false if constructed only when nonnull */
  public void setRequired(boolean required)
  {
    this.required = required;
  }

  /**
   * 
   */
  @Override
  public Bool3 evaluatesChildOnce(int i)
  {
    return Bool3.TRUE;
  }

  /**
   * Return the mapping table.
   */
  @Override
  public MappingTable getMappingTable()
  {
	  MappingTable mt = new MappingTable();

	  if (!(nameExpr() instanceof ConstExpr))
		  return mt;
	  
	  MappingTable child_table = valueExpr().getMappingTable();
	  ConstExpr ce = new ConstExpr(((ConstExpr)nameExpr()).value);
	  VarExpr ve = new VarExpr(new Var(MappingTable.DEFAULT_PIPE_VAR));
	  PathFieldValue pfv = new PathFieldValue(ce);
	  PathExpr pe = new PathExpr(ve, pfv);

	  if ((valueExpr() instanceof RecordExpr) || (valueExpr() instanceof ArrayExpr))
	  {
		  //Add the mapping at the field level: We need to change the AfterExpr in the mappings returned from the RecordExpr/ArrayExpr
		  Enumeration<Expr> e = child_table.KeyEnum();
		  while (e.hasMoreElements())
		  {
			  Expr after_expr = e.nextElement();
			  Expr before_expr = child_table.BeforeExpr(after_expr);
			  boolean safetyFlag = child_table.isSafeToMapExpr(after_expr);
			  
			  if (after_expr instanceof PathExpr)
			  {
				  VarMap vm = new VarMap();
				  ce = new ConstExpr(((ConstExpr)nameExpr()).value);
				  ve = new VarExpr(new Var(MappingTable.DEFAULT_PIPE_VAR));
				  PathStep ps = (PathStep)(((PathExpr)(after_expr)).firstStep()).clone(vm);
				  pfv = new PathFieldValue(ce, ps);
				  pe = new PathExpr(ve, pfv);
				  mt.add(pe, before_expr, safetyFlag);					  
			  }
		  }
	  }
	  else  //if ((valueExpr() instanceof PathExpr) || (valueExpr() instanceof VarExpr) || (valueExpr() instanceof ConstExpr) || (valueExpr() instanceof MathExpr)) 
		  mt.add(pe, valueExpr(), child_table.isSafeToMapAll());
		  
	  return mt;
  }

  
  /*
   * (non-Javadoc)
   * 
   * @see com.ibm.jaql.lang.expr.core.FieldExpr#decompile(java.io.PrintStream,
   *      java.util.HashSet)
   */
  public void decompile(FastPrinter exprText, HashSet<Var> capturedVars)
      throws Exception
  {
    exprText.print("(");
    exprs[0].decompile(exprText, capturedVars);
    exprText.print(")");
    if (!required)
    {
      exprText.print("?");
    }
    exprText.print(":(");
    exprs[1].decompile(exprText, capturedVars);
    exprText.print(")");
  }

  /*
   * (non-Javadoc)
   * 
   * @see com.ibm.jaql.lang.expr.core.FieldExpr#eval(com.ibm.jaql.lang.core.Context,
   *      com.ibm.jaql.json.type.MemoryJRecord)
   */
  public void eval(Context context, BufferedJsonRecord rec) throws Exception
  {
    JsonValue value = exprs[1].eval(context);
    if (required || value!=null)
    {
      // TODO: should we generate an error when the name is null or ignore the item?
      JsonString name = (JsonString) exprs[0].eval(context);
      if (name == null)
      {
        throw new NullPointerException("field name cannot be null");
      }
      rec.add(name, value);
    }
  }

}
