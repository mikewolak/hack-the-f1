/**
 * Femulator - MIDI Mapper and F1 Emulator control for Traktor
 * Copyright 2013, Andrew Bythell <abythell@ieee.org>
 * http://angryelectron.com/femulator
 *
 * Femulator is free software: you can redistribute it and/or modify it under
 * the terms of the GNU General Public License as published by the Free
 * Software Foundation, either version 3 of the License, or (at your option)
 * any later version.
 * 
 * Femulator is distributed in the hope that it will be useful, but WITHOUT ANY
 * WARRANTY; without even the implied warranty of MERCHANTABILITY or FITNESS
 * FOR A PARTICULAR PURPOSE.  See the GNU General Public License for more
 * details.
 *
 * You should have received a copy of the GNU General Public License along with
 * Femulator.  If not, see <http://www.gnu.org/licenses/>.
 */
package com.angryelectron.femulator.mapviewer;

import com.angryelectron.femulator.f1api.F1RefreshEvent;
import com.angryelectron.femulator.f1api.F1Service;
import com.angryelectron.femulator.f1api.F1Utils;
import java.util.Collection;
import org.netbeans.api.settings.ConvertAsProperties;
import org.openide.awt.ActionID;
import org.openide.awt.ActionReference;
import org.openide.explorer.ExplorerManager;
import org.openide.explorer.ExplorerUtils;
import org.openide.explorer.view.BeanTreeView;
import org.openide.util.Lookup.Result;
import org.openide.util.LookupEvent;
import org.openide.util.LookupListener;
import org.openide.util.NbBundle.Messages;
import org.openide.windows.TopComponent;

/**
 * A Top Component that implements an ExplorerManager with a ListView.
 */
@ConvertAsProperties(
    dtd = "-//com.angryelectron.femulator.mapviewer//Map//EN",
autostore = false)
@TopComponent.Description(
    preferredID = "MapTopComponent",
//iconBase="SET/PATH/TO/ICON/HERE", 
persistenceType = TopComponent.PERSISTENCE_ALWAYS)
@TopComponent.Registration(mode = "explorer", openAtStartup = true)
@ActionID(category = "Window", id = "com.angryelectron.femulator.mapviewer.MapTopComponent")
@ActionReference(path = "Menu/Window" /*, position = 333 */)
@TopComponent.OpenActionRegistration(
    displayName = "#CTL_MapAction",
preferredID = "MapTopComponent")
@Messages({
    "CTL_MapAction=Map",
    "CTL_MapTopComponent=Map Window",
    "HINT_MapTopComponent=This is a Map window"
})
public final class MapTopComponent extends TopComponent implements LookupListener, ExplorerManager.Provider {
              
    private ExplorerManager explorerMgr = new ExplorerManager();
    private Result<F1RefreshEvent> result;
    private F1Service f1;
    
    public MapTopComponent() {
        initComponents();
        setName(Bundle.CTL_MapTopComponent());
        setToolTipText(Bundle.HINT_MapTopComponent());
        putClientProperty(TopComponent.PROP_CLOSING_DISABLED, Boolean.TRUE);
        putClientProperty(TopComponent.PROP_DRAGGING_DISABLED, Boolean.TRUE);
        putClientProperty(TopComponent.PROP_MAXIMIZATION_DISABLED, Boolean.TRUE);
        putClientProperty(TopComponent.PROP_UNDOCKING_DISABLED, Boolean.TRUE);
        
        associateLookup(ExplorerUtils.createLookup(explorerMgr, getActionMap()));          
        explorerMgr.setRootContext(new DeviceNode());
        f1 = F1Utils.getF1Service();
    }

    /**
     * This method is called from within the constructor to initialize the form.
     * WARNING: Do NOT modify this code. The content of this method is always
     * regenerated by the Form Editor.
     */
    // <editor-fold defaultstate="collapsed" desc="Generated Code">//GEN-BEGIN:initComponents
    private void initComponents() {

        jScrollPane2 = new BeanTreeView();

        setLayout(new java.awt.BorderLayout());
        add(jScrollPane2, java.awt.BorderLayout.CENTER);
    }// </editor-fold>//GEN-END:initComponents

    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JScrollPane jScrollPane2;
    // End of variables declaration//GEN-END:variables
    
    void writeProperties(java.util.Properties p) {
        // better to version settings since initial version as advocated at
        // http://wiki.apidesign.org/wiki/PropertyFiles
        p.setProperty("version", "1.0");
        // TODO store your settings
    }

    void readProperties(java.util.Properties p) {
        String version = p.getProperty("version");
        // TODO read your settings according to their version
    }   

    @Override
    public ExplorerManager getExplorerManager() {
        return explorerMgr;
    }  

    @Override
    protected void componentOpened() {
        result = f1.getLookup().lookupResult(F1RefreshEvent.class);
        result.addLookupListener(this);
    }

    @Override
    protected void componentClosed() {
        result.removeLookupListener(this);
    }
    

    @Override
    public void resultChanged(LookupEvent ev) {
        Collection<? extends F1RefreshEvent> allInstances = result.allInstances();            
            if (!allInstances.isEmpty()) {            
                F1RefreshEvent event = allInstances.iterator().next();
                if (event.isLoadEvent()) {                      
                    explorerMgr.setRootContext(new DeviceNode(f1.getDevice()));
                }
            }
    }
    
}
