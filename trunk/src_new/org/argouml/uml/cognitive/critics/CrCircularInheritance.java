// Copyright (c) 1996-99 The Regents of the University of California. All
// Rights Reserved. Permission to use, copy, modify, and distribute this
// software and its documentation without fee, and without a written
// agreement is hereby granted, provided that the above copyright notice
// and this paragraph appear in all copies.  This software program and
// documentation are copyrighted by The Regents of the University of
// California. The software program and documentation are supplied "AS
// IS", without any accompanying services from The Regents. The Regents
// does not warrant that the operation of the program will be
// uninterrupted or error-free. The end-user understands that the program
// was developed for research purposes and is advised not to rely
// exclusively on the program for any reason.  IN NO EVENT SHALL THE
// UNIVERSITY OF CALIFORNIA BE LIABLE TO ANY PARTY FOR DIRECT, INDIRECT,
// SPECIAL, INCIDENTAL, OR CONSEQUENTIAL DAMAGES, INCLUDING LOST PROFITS,
// ARISING OUT OF THE USE OF THIS SOFTWARE AND ITS DOCUMENTATION, EVEN IF
// THE UNIVERSITY OF CALIFORNIA HAS BEEN ADVISED OF THE POSSIBILITY OF
// SUCH DAMAGE. THE UNIVERSITY OF CALIFORNIA SPECIFICALLY DISCLAIMS ANY
// WARRANTIES, INCLUDING, BUT NOT LIMITED TO, THE IMPLIED WARRANTIES OF
// MERCHANTABILITY AND FITNESS FOR A PARTICULAR PURPOSE. THE SOFTWARE
// PROVIDED HEREUNDER IS ON AN "AS IS" BASIS, AND THE UNIVERSITY OF
// CALIFORNIA HAS NO OBLIGATIONS TO PROVIDE MAINTENANCE, SUPPORT,
// UPDATES, ENHANCEMENTS, OR MODIFICATIONS.



// File: CrCircularInheritance.java
// Classes: CrCircularInheritance
// Original Author: jrobbins@ics.uci.edu
// $Id$

package org.argouml.uml.cognitive.critics;

import org.apache.log4j.Category;
import org.argouml.cognitive.Designer;
import org.argouml.cognitive.ToDoItem;
import org.argouml.cognitive.critics.Critic;
import org.argouml.model.ModelFacade;
import org.argouml.model.uml.foundation.core.CoreHelper;
import org.argouml.uml.SuperclassGen;
import org.tigris.gef.util.VectorSet;

import ru.novosoft.uml.foundation.core.MGeneralizableElement;

/** Well-formedness rule [2] for MGeneralizableElement. See page 31 of UML 1.1
 *  Semantics. OMG document ad/97-08-04. */

public class CrCircularInheritance extends CrUML {
    protected static Category cat = Category.getInstance(CrCircularInheritance.class);

  public CrCircularInheritance() {
    setHeadline("Remove <ocl>self</ocl>'s Circular Inheritance");
    setPriority(ToDoItem.HIGH_PRIORITY);
    addSupportedDecision(CrUML.decINHERITANCE);
    setKnowledgeTypes(Critic.KT_SYNTAX);
    addTrigger("generalization");
    // no need for trigger on "specialization"
  }

  public boolean predicate2(Object dm, Designer dsgr) {
      boolean problem = NO_PROBLEM;
      if (ModelFacade.isAGeneralizableElement(dm)) {
          try {
              CoreHelper.getHelper().getChildren(dm);
          }
          catch (IllegalStateException ex) {
              problem = PROBLEM_FOUND;
          }
      }
      return problem;
  }

  public ToDoItem toDoItem(Object dm, Designer dsgr) {
    MGeneralizableElement ge = (MGeneralizableElement) dm;
    VectorSet offs = computeOffenders(ge);
    return new ToDoItem(this, offs, dsgr);
  }

  protected VectorSet computeOffenders(MGeneralizableElement dm) {
    VectorSet offs = new VectorSet(dm);
    VectorSet above = offs.reachable(new SuperclassGen());
    java.util.Enumeration enum = above.elements();
    while (enum.hasMoreElements()) {
      MGeneralizableElement ge2 = (MGeneralizableElement) enum.nextElement();
      VectorSet trans = (new VectorSet(ge2)).reachable(new SuperclassGen());
      if (trans.contains(dm)) offs.addElement(ge2);
    }
    return offs;
  }

  public boolean stillValid(ToDoItem i, Designer dsgr) {
    if (!isActive()) return false;
    VectorSet offs = i.getOffenders();
    MGeneralizableElement dm = (MGeneralizableElement) offs.firstElement();
    if (!predicate(dm, dsgr)) return false;
    VectorSet newOffs = computeOffenders(dm);
    boolean res = offs.equals(newOffs);
    cat.debug("offs="+ offs.toString() +
	      " newOffs="+ newOffs.toString() +
	      " res = " + res);
    return res;
  }

} /* end class CrCircularInheritance.java */

