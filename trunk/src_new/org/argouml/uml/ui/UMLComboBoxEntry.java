// $Id$
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

package org.argouml.uml.ui;
import org.argouml.uml.*;
import java.util.*;
import org.argouml.model.ModelFacade;
import org.argouml.model.uml.UmlFactory;

public class UMLComboBoxEntry implements Comparable {
    private Object/*MModelElement*/ element;
    private String shortName;
    
    /** _longName is composed of an identifier and a name as in Class: String */
    private String longName;
    private Profile profile;
    
    /** _display name will be the same as shortName unless there 
     *  is a name collision */
    private String displayName;
    
    /** i am not quite sure what _isPhantom means, it may be that it is an
     *  entry that is not in the model list...pjs */
    private boolean thisIsAPhantom;

    /**
     * The constructor.
     * 
     * @param modelElement the model element
     * @param theProfile the profile
     * @param isPhantom 
     */
    public UMLComboBoxEntry(Object/*MModelElement*/ modelElement, 
            Profile theProfile, boolean isPhantom) {
        element = modelElement;
        if (modelElement != null) {
            Object/*MNamespace*/ ns = ModelFacade.getNamespace(modelElement);
            shortName = theProfile.formatElement(modelElement, ns);
        }
        else {
            shortName = "";
        }


        //
        //   format the element in its own namespace
        //       should result in an name without packages
        profile = theProfile;
        longName = null;
        displayName = shortName;
        thisIsAPhantom = isPhantom;
    }

    /**
     * @see java.lang.Object#toString()
     */
    public String toString() {
        return displayName;
    }

    public void updateName() {
        if (element != null) {
            Object/*MNamespace*/ ns = ModelFacade.getNamespace(element);
            shortName = profile.formatElement(element, ns);
        }
    }

    public void checkCollision(String before, String after) {
        boolean collision = (before != null && before.equals(shortName)) 
            || (after != null && after.equals(shortName));
        if (collision) {
            if (longName == null) {
                longName = getLongName();
            }
            displayName = longName;
        }
    }

    public String getShortName() {
        return shortName;
    }

    public String getLongName() {
        if (longName == null) {
            if (element != null) {
                longName = profile.formatElement(element, null);
            }
            else {
                longName = "void";
            }
        }
        return longName;
    }

    // Refactoring: static to denote that it doesn't use any class members.
    // TODO:
    // Idea to move this to MMUtil together with the same function from
    // org/argouml/uml/cognitive/critics/WizOperName.java
    // org/argouml/uml/generator/ParserDisplay.java
    private static Object findNamespace(Object/*MNamespace*/ phantomNS, 
            Object/*MModel*/ targetModel) {
        Object/*MNamespace*/ ns = null;
        Object/*MNamespace*/ targetParentNS = null;
        Object/*MNamespace*/ parentNS = ModelFacade.getNamespace(phantomNS);
        if (parentNS == null) {
            ns = targetModel;
        }
        else {
            targetParentNS = findNamespace(parentNS, targetModel);
            //
            //   see if there is already an element with the same name
            //
            Collection ownedElements = 
                ModelFacade.getOwnedElements(targetParentNS);
            String phantomName = ModelFacade.getName(phantomNS);
            String targetName;
            if (ownedElements != null) {
                Object/*MModelElement*/ ownedElement;
                Iterator iter = ownedElements.iterator();
                while (iter.hasNext()) {
                    ownedElement = iter.next();
                    targetName = ModelFacade.getName(ownedElement);
                    if (targetName != null && phantomName.equals(targetName)) {
                        if (ModelFacade.isAPackage(ownedElement)) {
                            ns = ownedElement;
                            break;
                        }
                    }
                }
            }
            if (ns == null) {
                ns = UmlFactory.getFactory().getModelManagement()
                    .createPackage();
                ModelFacade.setName(ns, phantomName);
                ModelFacade.addOwnedElement(targetParentNS, ns);
            }
        }
        return ns;
    }

    public Object/*MModelElement*/ getElement(Object targetModel) {
        //
        //  if phantom then
        //    we need to possibly recreate the package structure
        //       in the target model
        if (thisIsAPhantom && targetModel != null) {
            Object/*MNamespace*/ targetNS = 
                findNamespace(ModelFacade.getNamespace(element), targetModel);
            Object/*MModelElement*/ clone = null;
            try {
                clone = element.getClass().getConstructor(
                        new Class[] {}).newInstance(new Object[] {});
                ModelFacade.setName(clone, ModelFacade.getName(element));
                Object stereo = null;
                if (ModelFacade.getStereotypes(element).size() > 0) {
                    stereo = 
                        ModelFacade.getStereotypes(element).iterator().next();
                }
                ModelFacade.setStereotype(clone, stereo);
                if (ModelFacade.isAStereotype(clone)) {
                    ModelFacade.setBaseClass(clone, 
                            ModelFacade.getBaseClass(element));
                }
                ModelFacade.addOwnedElement(targetNS, clone);
                element = clone;
            }
            catch (Exception ex) {
                ex.printStackTrace();
            }
            thisIsAPhantom = false;
        }
        return element;
    }


    public void setElement(Object/*MModelElement*/ modelElement, 
            boolean isPhantom) {
        element = modelElement;
        thisIsAPhantom = isPhantom;
    }

    public int compareTo(final java.lang.Object other) {
        int compare = -1;
        if (other instanceof UMLComboBoxEntry) {
            UMLComboBoxEntry otherEntry = (UMLComboBoxEntry) other;
            compare = 0;
            if (otherEntry != this) {
                //
                //  if this is a "void" entry it goes first
                //
                if (element == null) {
                    compare = -1;
                }
                else {
                    //
                    //  if the other one is "void" it goes first
                    //
                    if (otherEntry.getElement(null) == null) {
                        compare = 1;
                    }
                    else {
                        //
                        //   compare short names
                        //
                        compare = getShortName()
                            .compareTo(otherEntry.getShortName());
                        //
                        //   compare long names
                        //
                        if (compare == 0) {
                            compare = getLongName()
                                .compareTo(otherEntry.getLongName());
                        }
                    }
                }
            }
        }
        return compare;
    }

    public void nameChanged(Object/*MModelElement*/ modelElement) {
        if (modelElement == element && element != null) {
            Object/*MNamespace*/ ns = ModelFacade.getNamespace(element);
            shortName = profile.formatElement(element, ns);
            displayName = shortName;
            longName = null;
        }
    }

    public boolean isPhantom() {
        return thisIsAPhantom;
    }
}