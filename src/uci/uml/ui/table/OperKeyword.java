// Copyright (c) 1996-98 The Regents of the University of California. All
// Rights Reserved. Permission to use, copy, modify, and distribute this
// software and its documentation for educational, research and non-profit
// purposes, without fee, and without a written agreement is hereby granted,
// provided that the above copyright notice and this paragraph appear in all
// copies. Permission to incorporate this software into commercial products
// must be negotiated with University of California. This software program and
// documentation are copyrighted by The Regents of the University of
// California. The software program and documentation are supplied "as is",
// without any accompanying services from The Regents. The Regents do not
// warrant that the operation of the program will be uninterrupted or
// error-free. The end-user understands that the program was developed for
// research purposes and is advised not to rely exclusively on the program for
// any reason. IN NO EVENT SHALL THE UNIVERSITY OF CALIFORNIA BE LIABLE TO ANY
// PARTY FOR DIRECT, INDIRECT, SPECIAL, INCIDENTAL, OR CONSEQUENTIAL DAMAGES,
// INCLUDING LOST PROFITS, ARISING OUT OF THE USE OF THIS SOFTWARE AND ITS
// DOCUMENTATION, EVEN IF THE UNIVERSITY OF CALIFORNIA HAS BEEN ADVISED OF THE
// POSSIBILITY OF SUCH DAMAGE. THE UNIVERSITY OF CALIFORNIA SPECIFICALLY
// DISCLAIMS ANY WARRANTIES, INCLUDING, BUT NOT LIMITED TO, THE IMPLIED
// WARRANTIES OF MERCHANTABILITY AND FITNESS FOR A PARTICULAR PURPOSE. THE
// SOFTWARE PROVIDED HEREUNDER IS ON AN "AS IS" BASIS, AND THE UNIVERSITY OF
// CALIFORNIA HAS NO OBLIGATIONS TO PROVIDE MAINTENANCE, SUPPORT, UPDATES,
// ENHANCEMENTS, OR MODIFICATIONS.

package uci.uml.ui.table;

import java.util.*;
import java.beans.*;

import uci.uml.Foundation.Core.*;
import uci.uml.Foundation.Data_Types.*;

public class OperKeyword implements java.io.Serializable {
  public static final OperKeyword NONE = new OperKeyword("none");
  public static final OperKeyword STATIC = new OperKeyword("static");
  public static final OperKeyword FINAL = new OperKeyword("final");
  public static final OperKeyword STATFIN = new OperKeyword("static final");
  public static final OperKeyword SYNC = new OperKeyword("synchronized");
  public static final OperKeyword STSYNC = new OperKeyword("static sync"); 
  public static final OperKeyword FINSYNC = new OperKeyword("final sync"); 
  public static final OperKeyword SFSYNC = new OperKeyword("static final sync");


  public static final OperKeyword[] POSSIBLES = {
    NONE, STATIC, FINAL, STATFIN, SYNC, STSYNC, FINSYNC, SFSYNC };

  protected String _label = null;
  
  private OperKeyword(String label) { _label = label; }
  
  public static OperKeyword KeywordFor(Operation op) {
    ScopeKind sk = op.getOwnerScope();
    CallConcurrencyKind ck = op.getConcurrency();
    // needs-more-work final?
    if (CallConcurrencyKind.CONCURRENT.equals(ck)) {
      if (ScopeKind.CLASSIFIER.equals(ck)) return STATIC;
      return NONE;
    }
    else {
      if (ScopeKind.CLASSIFIER.equals(ck)) return STSYNC;
      return SYNC;
    }
  }
  
  public boolean equals(Object o) {
    if (!(o instanceof OperKeyword)) return false;
    String oLabel = ((OperKeyword)o)._label;
    return _label.equals(oLabel);
  }

  public int hashCode() { return _label.hashCode(); }
  
  public String toString() { return _label.toString(); }

  public void set(Operation target) {
    CallConcurrencyKind ck = CallConcurrencyKind.CONCURRENT;
    if (this == SYNC || this == STSYNC || this == FINSYNC ||
	this == SFSYNC)
      ck = CallConcurrencyKind.GUARDED;
    
    ScopeKind sk = ScopeKind.INSTANCE;
    if (this == STATIC || this == STATFIN || this == STSYNC ||
	this == SFSYNC)
      sk = ScopeKind.CLASSIFIER;
      //needs-more-work: final
      
    try {
      target.setConcurrency(ck);
      target.setOwnerScope(sk);
      // needs-more-work: final
    }
    catch (PropertyVetoException pve) {
      System.out.println("could not set operation keywords");
    }
  }
} /* end class OperKeyword */
