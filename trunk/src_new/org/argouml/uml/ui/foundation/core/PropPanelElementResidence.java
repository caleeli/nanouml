// $Id$
// Copyright (c) 2007 The Regents of the University of California. All
// Rights Reserved. Permission to use, copy, modify, and distribute this
// software and its documentation without fee, and without a written
// agreement is hereby granted, provided that the above copyright notice
// and this paragraph appear in all copies. This software program and
// documentation are copyrighted by The Regents of the University of
// California. The software program and documentation are supplied "AS
// IS", without any accompanying services from The Regents. The Regents
// does not warrant that the operation of the program will be
// uninterrupted or error-free. The end-user understands that the program
// was developed for research purposes and is advised not to rely
// exclusively on the program for any reason. IN NO EVENT SHALL THE
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

package org.argouml.uml.ui.foundation.core;

import javax.swing.JList;
import javax.swing.JScrollPane;

import org.argouml.i18n.Translator;
import org.argouml.model.Model;
import org.argouml.uml.ui.ActionNavigateContainerElement;
import org.argouml.uml.ui.UMLLinkedList;
import org.argouml.uml.ui.UMLModelElementListModel2;
import org.argouml.util.ConfigLoader;

/**
 * The properties panel for a ElementResidence.
 * 
 * @author michiel
 */
public class PropPanelElementResidence extends PropPanelModelElement {

    /**
     * Construct a property panel for a ElementResidence.
     */
    public PropPanelElementResidence() {
        super("ElementResidence", ConfigLoader.getTabPropsOrientation());

         // This is not a ModelElement, hence none of the following:
//      addField(Translator.localize("label.name"),
//      getNameTextField());
//      add(getNamespaceVisibilityPanel());
//      addSeparator();

        JList lst1 = new UMLLinkedList(
                new ElementResidenceContainerListModel());
        lst1.setVisibleRowCount(1);
        addField(Translator.localize("label.container"),
                new JScrollPane(lst1));

        JList lst2 = new UMLLinkedList(
                new ElementResidenceResidentListModel());
        lst2.setVisibleRowCount(1);
        addField(Translator.localize("label.resident"),
                new JScrollPane(lst2));

        addAction(new ActionNavigateContainerElement());
        addAction(getDeleteAction());
    }

}

/**
 * The list model for the container of a ElementResidence.
 *
 * @author mvw
 */
class ElementResidenceContainerListModel extends UMLModelElementListModel2 {

    /**
     * Constructor for ElementResidenceContainerListModel.
     */
    public ElementResidenceContainerListModel() {
        super("container");
    }

    protected void buildModelList() {
        if (getTarget() != null) {
            removeAllElements();
            addElement(Model.getFacade().getContainer(getTarget()));
        }
    }

    protected boolean isValidElement(Object element) {
        return Model.getFacade().isAElementResidence(getTarget());
    }
}

/**
 * The list model for the resident of a ElementResidence of a diagram.
 *
 * @author mvw
 */
class ElementResidenceResidentListModel extends UMLModelElementListModel2 {

    /**
     * Constructor for ElementResidenceResidentListModel.
     */
    public ElementResidenceResidentListModel() {
        super("resident");
    }

    protected void buildModelList() {
        if (getTarget() != null) {
            removeAllElements();
            addElement(Model.getFacade().getResident(getTarget()));
        }
    }

    protected boolean isValidElement(Object element) {
        return Model.getFacade().isAElementResidence(getTarget());
    }
}