
/**
 * @class BaseViewer.BaseContainer
 * @extends Ext.tab.Panel
 *
 * A container class for showing JSP Pages
 * 
 * @constructor
 * Create a new Page container
 * @param {Object} config The config object
 */
Ext.define('BaseViewer.BaseContainer', {
    
    extend: 'Ext.tab.Panel',
    alias: 'widget.basecontainer',
    tabId: '', 
    tabContentEl: '', 
    tabTitle: '', 
    tabIconCls: '',
    
    initComponent: function() {
        Ext.apply(this,{
        	region: 'center', // a center region is ALWAYS required for border layout
            deferredRender: false/*,
            activeTab: 1,     // first tab initially active
            items: [{
                contentEl: 'container',
                title: 'Close Me',
                closable: true,
                autoScroll: true
            }, {
                contentEl: 'center2',
                title: 'Center Panel',
                autoScroll: true
            }] //Items center*/
        });
        
        if(this.tabContentEl && this.tabContentEl !=''){
        	this.addTab(this.tabId, this.tabContentEl, this.tabTitle, this.tabIconCls);
        }
        
        this.callParent();
    },
    
    
    /**
     * Add a new tab to the container
     * @param {String} tabId The id of the new tab
     * @param {String} contentId The id of the HTMLelement or Ext.Element to show within the tab
     * @param {String} title The title of the tab
     * @param {String} iconCls The CSS Class for the icon of the tab
     * @return null
     */
    addTab: function(tabId, contentId, title, iconCls){
    	this.add({
    		title: title,
    		id: tabId,
    		contentEl: contentId,
    		iconCls: iconCls
    	});
    }
});
