
/**
 * @class BaseViewer.BaseTop
 * @extends Ext.panel.Panel
 *
 * A container class for showing login and logout stuff on the top of the page
 * 
 * @constructor
 * Create a new Base Top section
 * @param {Object} config The config object
 */
Ext.define('BaseViewer.BaseTop', {
    //xtype:'box',
    extend: 'Ext.Component',
    alias: 'widget.basetop',
    
    region: 'north',
    height: 25, // give north and south regions a height
    style: {
    	'text-align':'right'
    },
    
    
    initComponent: function() {
        
        this.callParent();
    }
});



