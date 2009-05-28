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

import java.io.PrintStream;
import java.util.HashSet;

import com.ibm.jaql.json.type.Item;
import com.ibm.jaql.json.util.Iter;
import com.ibm.jaql.lang.core.Context;
import com.ibm.jaql.lang.core.Var;
import com.ibm.jaql.util.Bool3;

/*
 * 
 * Should: for $i in null if p($i) return e($i) be [] or null?
 * 
 * Right now it is []. But let's assume that it is null. There was some problem
 * with producing null, but I can't recall what. Let's see what we find:
 * 
 * for $i in null return $i => null for $i in e return $i ==> e (good!) instead
 * of for $i in e return $i ==> emptyOnNull(e) (ugly)
 * 
 * e1 in e2 ==> let $x = e1 return (for $i in e2 if $i == $x return true)[0]
 * (good) instead of let $x = e1, $a = e2 return if isnull($a) then null else
 * (for $i in e2 if $i == $x return true)[0] which is ugly and slow unless I
 * make a null-preserving FOR
 * 
 * 
 * for $i in [1,2], $j in (if $i == 1 then null else [null]) return $j ==>
 * unnest for $i in [1,2] return for $j in (if $i == 1 then null else [null])
 * return $j ==> unnest [for $j in null return $j, for $j in [null] return $j]
 * ==> unnest [null, [null]] // vs. unnest [[], [null]] = [null] using old def
 * ==> [null, null] (bad!!) instead of [null]
 * 
 * So now we need to change the definintion of unnest, add another flavor of
 * unnest, or add emptyOnNull between the FORs.
 * 
 * Let's try to define unnest to eliminate nulls.
 * 
 * unnest [1, [2,[3,4]], null, 5, [null]] ==> [1, 2, [3,4], 5, null]
 * 
 * unnest e ==> for $i in $e, $j in ( if $i instanceof type array then $i else
 * if isnull($i) then [] // the new addition else [$i] ) return $j ==> for $i in
 * $e
 * 
 */
public final class ForExpr extends IterExpr
{
  /**
   * BindingExpr inExpr, Expr collectExpr
   * 
   * @param exprs
   */
  public ForExpr(Expr[] exprs)
  {
    super(exprs);
  }

  /**
   * @param inBinding
   * @param collectExpr
   */
  public ForExpr(BindingExpr inBinding, Expr collectExpr)
  {
    this(new Expr[]{inBinding, collectExpr});
  }

  /**
   * @param mapVar
   * @param inExpr
   * @param collectExpr
   */
  public ForExpr(Var mapVar, Expr inExpr, Expr collectExpr)
  {
    this(new BindingExpr(BindingExpr.Type.IN, mapVar, null, inExpr),
        collectExpr);
  }

  /**
   * @return
   */
  public BindingExpr binding()
  {
    return (BindingExpr) exprs[0];
  }

  /**
   * @return
   */
  public Var var()
  {
    return binding().var;
  }

  /**
   * @return
   */
  public Expr collectExpr()
  {
    return exprs[1];
  }

  /*
   * (non-Javadoc)
   * 
   * @see com.ibm.jaql.lang.expr.core.Expr#isNull()
   */
  @Override
  public Bool3 isNull()
  {
    return Bool3.FALSE;
    // return binding().inExpr().isNull().or(collectExpr().isNull());
  }

  /*
   * (non-Javadoc)
   * 
   * @see com.ibm.jaql.lang.expr.core.Expr#decompile(java.io.PrintStream,
   *      java.util.HashSet)
   */
  public void decompile(PrintStream exprText, HashSet<Var> capturedVars)
      throws Exception
  {
    BindingExpr b = binding();
    exprText.print("\nfor( ");
    exprText.print(b.var.name);
    exprText.print(" in ");
    b.inExpr().decompile(exprText, capturedVars);
    exprText.println(" )");
    collectExpr().decompile(exprText, capturedVars);
    capturedVars.remove(b.var);
  }

  /*
   * (non-Javadoc)
   * 
   * @see com.ibm.jaql.lang.expr.core.IterExpr#iter(com.ibm.jaql.lang.core.Context)
   */
  public Iter iter(final Context context) throws Exception
  {
    final BindingExpr inBinding = binding();
    final Expr collectExpr = collectExpr();

    final Iter inIter = inBinding.inExpr().iter(context);

    //    // If the input is null, return null
    //    if( inIter.isNull() )
    //    {
    //      return Iter.nil;
    //    }
    //
    //    // If every iteration returns null, return null
    //    Iter iter;
    //    do
    //    {
    //      Item item = inIter.next();
    //      if( item == null )
    //      {
    //        return Iter.nil;
    //      }
    //      context.setVar(inBinding.var, item);
    //      iter = collectExpr.iter(context);
    //    }
    //    while( iter.isNull() );
    //    
    //    // Return a non-null result
    //    final Iter tmpIter = iter;

    return new Iter() {
      //      Iter collectIter = tmpIter;
      Iter collectIter = Iter.empty;

      public Item next() throws Exception
      {
        while (true)
        {
          Item item;
          while ((item = collectIter.next()) != null)
          {
            return item;
          }

          item = inIter.next();
          if (item == null)
          {
            return null;
          }
          context.setVar(inBinding.var, item);

          collectIter = collectExpr.iter(context);
        }
      }
    };
  }

}