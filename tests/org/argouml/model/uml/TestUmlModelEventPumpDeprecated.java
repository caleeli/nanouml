// $Id$
// Copyright (c) 2002-2005 The Regents of the University of California. All
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

package org.argouml.model.uml;

import java.lang.ref.WeakReference;
import java.util.Arrays;
import java.util.Date;

import junit.framework.TestCase;

import org.argouml.kernel.Project;
import org.argouml.kernel.ProjectManager;
import org.argouml.model.Model;

import ru.novosoft.uml.MElementEvent;
import ru.novosoft.uml.MElementListener;
import ru.novosoft.uml.MFactoryImpl;
import ru.novosoft.uml.foundation.core.MClass;
import ru.novosoft.uml.foundation.core.MClassImpl;
import ru.novosoft.uml.foundation.core.MModelElement;
import ru.novosoft.uml.foundation.core.MOperationImpl;
import ru.novosoft.uml.foundation.core.MParameter;
import ru.novosoft.uml.foundation.core.MParameterImpl;
import ru.novosoft.uml.model_management.MModel;

/**
 * @since Oct 15, 2002
 * @author jaap.branderhorst@xs4all.nl
 */
public class TestUmlModelEventPumpDeprecated extends TestCase {

    private MClass elem;
    private MockModelEventListener listener;
    private boolean eventcalled;
    private TestListener listener2;

    private static final boolean TEST_PERFORMANCE = false;
    private static final int PERFORMANCE_TEST_SIZE = 5000;

    private class TestListener implements MElementListener {

        /**
         * @see ru.novosoft.uml.MElementListener#listRoleItemSet(ru.novosoft.uml.MElementEvent)
         */
        public void listRoleItemSet(MElementEvent e) {
            eventcalled = true;
        }

        /**
         * @see ru.novosoft.uml.MElementListener#propertySet(ru.novosoft.uml.MElementEvent)
         */
        public void propertySet(MElementEvent e) {
            eventcalled = true;
        }

        /**
         * @see ru.novosoft.uml.MElementListener#recovered(ru.novosoft.uml.MElementEvent)
         */
        public void recovered(MElementEvent e) {
            eventcalled = true;
        }

        /**
         * @see ru.novosoft.uml.MElementListener#removed(ru.novosoft.uml.MElementEvent)
         */
        public void removed(MElementEvent e) {
            eventcalled = true;
        }

        /**
         * @see ru.novosoft.uml.MElementListener#roleAdded(ru.novosoft.uml.MElementEvent)
         */
        public void roleAdded(MElementEvent e) {
            eventcalled = true;
        }

        /**
         * @see ru.novosoft.uml.MElementListener#roleRemoved(ru.novosoft.uml.MElementEvent)
         */
        public void roleRemoved(MElementEvent e) {
            eventcalled = true;
        }

    }

    /**
     * Constructor for TestUmlModelEventPump.
     *
     * @param arg0 is the name of the test case.
     */
    public TestUmlModelEventPumpDeprecated(String arg0) {
        super(arg0);
    }

    /**
     * @see junit.framework.TestCase#setUp()
     */
    protected void setUp() throws Exception {
        super.setUp();
        MFactoryImpl.setEventPolicy(MFactoryImpl.EVENT_POLICY_IMMEDIATE);
        elem = (MClass) Model.getCoreFactory().createClass();
        Project project = ProjectManager.getManager().getCurrentProject();
        MModel model = (MModel) project.getRoot();
        model.addOwnedElement(elem);
        listener = new MockModelEventListener();
        eventcalled = false;
        listener2 = new TestListener();
    }

    /**
     * @see junit.framework.TestCase#tearDown()
     */
    protected void tearDown() throws Exception {
        super.tearDown();
        if (elem != null) {
            Model.getUmlFactory().delete(elem);
        }
        UmlModelEventPump.getPump().cleanUp();
        listener2 = null;
        listener = null;
    }

    /**
     * Test wether a listener can be registred to both the registry with class
     * modelevent listeners as the object modelevent listeners.
     */
    public void testAddLegalListener() {
        UmlModelEventPump.getPump().cleanUp();
        assertTrue(UmlModelEventPump.getPump().getEventListenerMap().isEmpty());
        // assertTrue(UmlModelEventPump.getPump()
        //     .getClassListenerMap().isEmpty());
        UmlModelEventPump.getPump().addClassModelEventListener(
							       listener,
							       elem.getClass(),
							       new String[] {
								   "name",
							       });
        UmlModelEventPump.getPump().addModelEventListener(
							  listener,
							  elem,
							  new String[] {
							      "name",
							  });
        assertTrue(
	    !UmlModelEventPump.getPump().getClassListenerMap().isEmpty());
        assertTrue(
	    !UmlModelEventPump.getPump().getEventListenerMap().isEmpty());
        // assertTrue(UmlModelEventPump.getPump().getClassListenerMap()
        //    .getListenerList(elem.getClass()).getListenerCount() > 0);
        MElementListener[] list =
            UmlModelEventPump.getPump()
	        .getEventListenerMap().getListeners(elem,
						    new EventKey(2, "name"));
        assertTrue(list.length > 0);
        assertTrue(Arrays.asList(list).contains(listener));
    }

    /**
     * Tests wether it is possible (it should not be) to give empty arguments
     * while registring a listener.
     */
    public void testAddEmptyParametersListener() {
        try {
            UmlModelEventPump.getPump()
		.addClassModelEventListener(null,
					    elem.getClass(),
					    new String[] {
						"name",
					    });
            fail();
        } catch (Exception ex) {
        }
        try {
            UmlModelEventPump.getPump()
		.addClassModelEventListener(listener,
					    null,
					    new String[] {
						"name",
					    });
            fail();
        } catch (Exception ex) {
        }
        try {
            UmlModelEventPump.getPump().addModelEventListener(
							      null,
							      elem,
							      new String[] {
								  "name",
							      });
            fail();
        } catch (Exception ex) {
        }
        try {
            UmlModelEventPump.getPump().addModelEventListener(
							      listener,
							      null,
							      new String[] {
								  "name",
							      });
        } catch (Exception ex) {
        }
    }

    /**
     * Tests if the association from a modelelement to the pump is thrown away
     * after deletion of the element.
     */
    public void testCreateDelete() {
        WeakReference ref = new WeakReference(elem);
        Model.getUmlFactory().delete(elem);
        elem = null;
        System.gc();
        assertNull(ref.get());
    }

    /**
     * Tests if a listener that registred for a PropertySet event on
     * the class level really received the event.
     */
    public void testPropertySetClass() {
        UmlModelEventPump.getPump().addClassModelEventListener(
							       listener2,
							       elem.getClass(),
							       new String[] {
								   "isRoot" 
							       });
        elem.setRoot(true);
        assertTrue(eventcalled);
    }

    /**
     * Tests if a listener that registred for a ListRoleItemSet event
     * on the class level really received the event.
     */
    public void testRecoveredClass() {
        // this is never done, not by NSUML and not by Argo... 
        // therefore no implementation possible
    }

    /**
     * Tests if a listener that registred for a Removed event on the
     * class level really received the event.
     */
    public void testRemovedClass() {
        UmlModelEventPump.getPump().addClassModelEventListener(
							       listener2,
							       elem.getClass(),
							       new String[] {
								   "remove" 
							       });
        elem.remove();
        assertTrue(eventcalled);
    }

    /**
     * Tests if a listener that registred for a RoleAdded event on the
     * class level really received the event.
     */
    public void testRoleAddedSetClass() {
        UmlModelEventPump.getPump().addClassModelEventListener(
							       listener2,
							       elem.getClass(),
							       new String[] {
								   "parameter" 
							       });
        elem.addParameter(new MParameterImpl());
        assertTrue(eventcalled);
    }

    /**
     * Tests if a listener that registred for a RoleRemoved event on
     * the class level really received the event.
     */
    public void testRoleRemovedSetClass() {
        MParameter param = new MParameterImpl();
        elem.addParameter(param);
        UmlModelEventPump.getPump().addClassModelEventListener(
							       listener2,
							       elem.getClass(),
							       new String[] {
								   "parameter" 
							       });
        elem.removeParameter(param);
        assertTrue(eventcalled);
    }

    /**
     * Tests if a non registred listener does not receive any events (never can
     * be too sure :))
     */
    public void testFireNonRegistredListener() {
        MClass elem2 = (MClass) Model.getCoreFactory().createClass();
        elem.addParameter(new MParameterImpl());
        assertTrue(!eventcalled);
    }

    /**
     * Tests if a listener that registred for a ListRoleItemSet event really
     * received the event.
     */
    public void testListRoleItemSet() {
        elem.addFeature(new MOperationImpl());
        UmlModelEventPump.getPump().addModelEventListener(
							  listener2,
							  elem,
							  new String[] {
							      "feature" 
							  });
        elem.setFeature(0, new MOperationImpl());
        assertTrue(eventcalled);
    }

    /**
     * Tests if a listener that registred for a PropertySet event really
     * received the event.
     */
    public void testPropertySet() {
        UmlModelEventPump.getPump().addModelEventListener(
							  listener2,
							  elem,
							  new String[] {
							      "isRoot" 
							  });
        elem.setRoot(true);
        assertTrue(eventcalled);
    }

    /**
     * Tests if a listener that registred for a Recovered event really
     * received the event.
     */
    public void testRecovered() {
        // this is never done, not by NSUML and not by Argo... 
        // therefore no implementation possible
    }

    /**
     * Tests if a listener that registred for a Removed event really
     * received the event.
     */
    public void testRemoved() {
        UmlModelEventPump.getPump()
	    .addModelEventListener(listener2,
				   elem,
				   new String[] {
				       UmlModelEventPump.REMOVE 
				   });
        elem.remove();
        assertTrue(eventcalled);
    }

    /**
     * Tests if a listener that registred for a RoleAddedSet event really
     * received the event.
     */
    public void testRoleAddedSet() {
        UmlModelEventPump.getPump().addModelEventListener(
							  listener2,
							  elem,
							  new String[] {
							      "parameter" 
							  });
        elem.addParameter(new MParameterImpl());
        assertTrue(eventcalled);
    }

    /**
     * Tests if a listener that registred for a RoleRemovedSet event really
     * received the event.
     */
    public void testRoleRemovedSet() {
        MParameter param = new MParameterImpl();
        elem.addParameter(param);
        UmlModelEventPump.getPump().addModelEventListener(
							  listener2,
							  elem,
							  new String[] {
							      "parameter" 
							  });
        elem.removeParameter(param);
        assertTrue(eventcalled);
    }

    /**
     * Tests if a listener to a class that is legally added and then
     * removed, really is removed.
     */
    public void testRemoveLegalClassListener() {
        UmlModelEventPump.getPump().addClassModelEventListener(
							       listener2,
							       elem.getClass(),
							       new String[] {
								   "isRoot" 
							       });
        UmlModelEventPump.getPump()
	    .removeClassModelEventListener(listener2,
					   elem.getClass(),
					   new String[] {
					       "isRoot" 
					   });
        assertEquals(0,
		     UmlModelEventPump.getPump()
		     .getClassListenerMap()
		     .getListeners(elem.getClass(),
				   new EventKey(2, "isRoot"))
		     .length);
        elem.addParameter(new MParameterImpl());
        assertTrue(!eventcalled);
    }

    /**
     * Tests if a listener that is legally added and then removed,
     * really is removed.
     */
    public void testRemoveLegalListener() {
        
        int existingListeners = UmlModelEventPump.getPump()
                                    .getEventListenerMap().getListeners(
                                        elem,
					new EventKey(2, "isRoot")).length;
        
        UmlModelEventPump.getPump().addModelEventListener(
							  listener2,
							  elem,
							  new String[] {
							      "isRoot" 
							  });
        UmlModelEventPump.getPump().removeModelEventListener(
							     listener2,
							     elem,
							     new String[] {
								 "isRoot"
							     });
        assertEquals(existingListeners,
		     UmlModelEventPump.getPump()
		     .getEventListenerMap()
		     .getListeners(elem,
				   new EventKey(2, "isRoot"))
		     .length);
        elem.addParameter(new MParameterImpl());
        assertTrue(!eventcalled);
    }

    /**
     * Tests if the hashmap with listeners not is empty after removing one 
     * listener but not all listeners.
     */
    public void testRemoveListenerNonEmptyClass() {
        UmlModelEventPump.getPump().addClassModelEventListener(
							       listener2,
							       elem.getClass(),
							       new String[] {
								   "isRoot" 
							       });
        UmlModelEventPump.getPump().addClassModelEventListener(
							       listener,
							       elem.getClass(),
							       new String[] {
								   "isRoot"
							       });
        UmlModelEventPump.getPump()
	    .removeClassModelEventListener(listener,
					   elem.getClass(),
					   new String[] {
					       "isRoot"
					   });
        assertTrue((UmlModelEventPump.getPump()
		    .getEventListenerMap()
		    .getListeners(elem,
				  new EventKey(2, "isRoot"))
		    .length)
		   > 0);
    }

    /**
     * Tests if the hashmap with listeners not is empty after removing one 
     * listener but not all listeners.
     */
    public void testRemoveListenerNonEmpty() {
        UmlModelEventPump.getPump().addModelEventListener(
							  listener2,
							  elem,
							  new String[] {
							      "isRoot"
							  });
        UmlModelEventPump.getPump().addModelEventListener(
							  listener,
							  elem,
							  new String[] {
							      "isRoot"
							  });
        UmlModelEventPump.getPump().removeModelEventListener(
							     listener,
							     elem,
							     new String[] {
								 "isRoot"
							     });
        assertTrue((UmlModelEventPump.getPump()
		    .getEventListenerMap()
		    .getListeners(elem,
				  new EventKey(2, "isRoot"))
		    .length)
		   > 0);
    }

    // the behavior that a registred class not only listens to its own
    // events (for instance a MModelelement listens to name events and
    // to events fired by MElement) is much too powerfull and will
    // cost a lot of performance. Therefore I removed this feature
    /*
      public void testListensDependencyToSuperClass() {
      MDependency dep = CoreFactory.getFactory().createDependency();
      UmlModelEventPump.getPump().addClassModelEventListener(listener2, 
          dep.getClass(), "behavior");
      dep.addBehavior(StateMachinesFactory.getFactory().createStateMachine());
      assertTrue(eventcalled);
      }
    
      public void testListensSuperClassToDependency() {
      MDependency dep = CoreFactory.getFactory().createDependency();
      UmlModelEventPump.getPump().addClassModelEventListener(listener2, 
          MModelElement.class, "behavior");
      dep.addBehavior(StateMachinesFactory.getFactory().createStateMachine());
      assertTrue(eventcalled);
      }
    */

    /**
     * Tests if a listener that registered for all events for some modelClass
     * receives all events
     */
    public void testRecoveredClass2() {
        // this is never done, not by NSUML and not by Argo... 
        // therefore no implementation possible
    }

    /**
     * Performance test for adding and removing listeners, only run when
     * TEST_PERFORMANCE == true
     */
    public void testAddRemoveListenersPerformance() {
        if (TEST_PERFORMANCE) {
            MModelElement element = new MParameterImpl();
            TestListener[] listeners = new TestListener[PERFORMANCE_TEST_SIZE];
	    UmlModelEventPump pump = UmlModelEventPump.getPump();
            Date addTime = new Date();
            for (int i = 0; i < PERFORMANCE_TEST_SIZE; i++) {
                listeners[i] = new TestListener();
                pump.addModelEventListener(listeners[i],
					   element,
					   "state");
            }
            Date removeTime = new Date();
            for (int i = 0; i < PERFORMANCE_TEST_SIZE; i++) {
                pump.removeModelEventListener(listeners[i],
					      element,
					      "state");
            }
            Date endTime = new Date();
            long timeElapsed = removeTime.getTime() - addTime.getTime();
            System.out.println("Time elapsed while adding listeners: "
			       + timeElapsed);
            timeElapsed = endTime.getTime() - removeTime.getTime();
            System.out.println("Time elapsed while removing listeners: "
			       + timeElapsed);
        }
    }

    /**
     * Tests the performance of firing roleremoved events to a single listener
     * from a single source.
     */
    /*
      public void testRoleRemovedPerfomance() {
      if (TEST_PERFORMANCE) {
      MClass clazz = new MClassImpl();
      MParameter[] parameters = new MParameter[PERFORMANCE_TEST_SIZE];
      MFactoryImpl.setEventPolicy(MFactoryImpl.EVENT_POLICY_DISABLED);
      for (int i = 0; i < PERFORMANCE_TEST_SIZE; i++) {
      parameters[i] = new MParameterImpl();

      clazz.addParameter(parameters[i]);
      UmlModelEventPump.getPump().addModelEventListener(
      listener2,
      clazz,
      new String[] { "parameter" });
      }
      MFactoryImpl.setEventPolicy(MFactoryImpl.EVENT_POLICY_IMMEDIATE);
      Date currentTime = new Date();
      for (int i = 0; i < PERFORMANCE_TEST_SIZE; i++) {
      clazz.removeParameter(parameters[i]);
      }
      long timeElapsed = (new Date()).getTime() - currentTime.getTime();
      System.out.println(
      "Time elapsed while firing "
      + PERFORMANCE_TEST_SIZE
      + " RoleRemoved events: "
      + timeElapsed);
      }
      }
    */
    /**
     * Tests the performance of the eventpump of firing roleremoved
     * events to a single listener but from 100 * TEST_PERFORMANCE
     * modelelements.
     *
     */
    public void testRoleRemovedPerformance2() {
        if (TEST_PERFORMANCE) {
            MClass[] clazz = new MClassImpl[PERFORMANCE_TEST_SIZE];
            MParameter[] parameters =
                new MParameter[PERFORMANCE_TEST_SIZE];
            MFactoryImpl.setEventPolicy(MFactoryImpl.EVENT_POLICY_DISABLED);
            for (int i = 0; i < PERFORMANCE_TEST_SIZE; i++) {
                parameters[i] = new MParameterImpl();
                clazz[i] = new MClassImpl();
                clazz[i].addParameter(parameters[i]);
                UmlModelEventPump.getPump()
		    .addModelEventListener(listener2,
					   clazz[i],
					   new String[] {
					       "parameter"
					   });
            }
            MFactoryImpl.setEventPolicy(MFactoryImpl.EVENT_POLICY_IMMEDIATE);
            Date currentTime = new Date();
            for (int i = 0; i < PERFORMANCE_TEST_SIZE; i++) {
                clazz[i].removeParameter(parameters[i]);
            }
            long timeElapsed = (new Date()).getTime() - currentTime.getTime();
            System.out.println(
			       "Time elapsed while firing "
			       + PERFORMANCE_TEST_SIZE
			       + " RoleRemoved events: "
			       + timeElapsed);
        }
    }

}
