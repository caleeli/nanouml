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

package org.argouml.uml.diagram.sequence2.ui;

import java.awt.event.MouseEvent;
import java.util.List;

import org.tigris.gef.base.Layer;
import org.tigris.gef.base.ModePlace;
import org.tigris.gef.graph.GraphFactory;
import org.tigris.gef.presentation.Fig;

/**
 *
 * @author penyaskito
 */
public class ModePlaceClassifierRole extends ModePlace {
    
    /**
     * @param gf The GraphFactory
     * @param instructions A string with the instructions for the tooltip
     */
    public ModePlaceClassifierRole(GraphFactory gf, String instructions) {
        super(gf, instructions);
    }
    
    @Override
    public void mousePressed(MouseEvent me) {
        super.mousePressed(me);
        postProcessing();
    }

    @Override
    public void mouseDragged(MouseEvent me) {
        super.mouseDragged(me);
        postProcessing();
    }

    
    private void  postProcessing() {
        // _pers is the FigClassifierRole we're trying to place
        int y = _pers.getY();
        Layer lay = editor.getLayerManager().getActiveLayer();
        List nodes = lay.getContentsNoEdges();
        // Get the first existing FigNode and if it exists set the
        // y position of _pers to be the same as it.
        // TODO: What if the first node is not a FigClassifierRole but is a
        // FigComment. Make this safe for that.
        // TODO: We should also fix height as well as y position here.
        if (nodes.size() > 0) {
            Fig fig = (Fig) nodes.get(0);
            if (fig != _pers) {
                y = fig.getY();
                _pers.setY(y);
            }
        }
    }
}
