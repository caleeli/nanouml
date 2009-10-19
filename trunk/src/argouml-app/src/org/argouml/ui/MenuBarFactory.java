// $Id:GenericArgoMenuBar.java 13104 2007-07-21 18:29:31Z mvw $
// Copyright (c) 1996,2009 The Regents of the University of California. All
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

package org.argouml.ui;

import org.argouml.model.Model;
import org.argouml.ui.cmd.GenericArgoMenuBar;

/**
 * A factory to create the menu bar for the application
 * @since 0.29.2
 */
class MenuBarFactory {
    
    /**
     * Build the menu "Create" and the toolbar for diagram creation. These are
     * build together to guarantee that the same items are present in both, and
     * in the same sequence.
     * <p>
     * 
     * The sequence of these items was determined by issue 1821.
     */
    static GenericArgoMenuBar createApplicationMenuBar() {
        if (Model.getFacade().getUmlVersion().charAt(0) == '2') {
            return new MenuBar2();
        } else {
            return new MenuBar14();
        }
    }
}
