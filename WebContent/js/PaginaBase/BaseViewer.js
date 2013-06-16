/**
 * @class BaseViewer.BaseViewer
 * @extends Ext.container.Viewport
 * @author HWM
 *
 * The main page application with dinamic menu
 * 
 * @constructor
 * Create a new base page app
 * @param {Object} config The config object
 */

Ext.define('BaseViewer.App', {
    extend: 'Ext.container.Viewport',
    
    /**
     * contentEl attribute for BaseTopPanel
     */
    contentElTop:'', 
    
    /**
     * Id of the Tab 
     */
    tabId: '',
    
    /**
     * ContentEl attribute for BaseContainer. It's the tab show on screen 
     */
    tabContentEl: '', 
    
    /**
     * Title of the tab
     */
    tabTitle: '',
    
    /**
     * CSS apply for the icon of the tab
     */
    tabIconCls: '',
    
    /**
     * String containin text to display at the top of the menu panel
     */
    menuTitle: '',
    
    onMenuCollapse: function(){return;},
    onMenuExpand: function(){return;},
    
    initComponent: function(){
        
    	Ext.define('OptionGroup',{
        	extend: 'Ext.data.Model',
        	fields: [
        		{name: 'title', type:'string'}, 
        		{name:'url', type:'string'},
        		{name:'css', type:'string'}
        	]
        });
        
        Ext.apply(this, {
            layout: 'border',
            padding: 5,
            items: [this.createBaseTop(),
                    this.createBaseMenu(),
		            this.createBaseContainer()
                    /*,
                    this.createFeedPanel(), 
                    this.createFeedInfo(),*/
                    
             ]
        });
        this.callParent(arguments);
    },
    
    /**
     * Create the tab panel used to show the JSP Pages
     * @private
     * @return {BaseViewer.BaseContainer} baseContainer
     */
    createBaseContainer: function(){
        if(this.tabContentEl && this.tabContentEl != ''){
	    	this.baseContainer = Ext.create('widget.basecontainer', {
	        	tabId: this.tabId,
	        	tabContentEl: this.tabContentEl,
	        	tabTitle: this.tabTitle, 
	        	tabIconCls: this.tabIconCls
	        });
        }else{
        	this.baseContainer = Ext.create('widget.basecontainer');
        }
        return this.baseContainer;
    },
    
    /**
     * Create the Base Top Section
     * @private
     * @return {BaseViewer.BaseTop} baseTop
     */
    createBaseTop: function(){
        this.baseTop = Ext.create('widget.basetop', {
            contentEl: this.contentElTop
        });
        return this.baseTop;
    },
    

    /**
     * Docked panel for menu
     * @private
     * @return {BaseViewer.BaseMenu} baseMenu object
     */
    createBaseMenu: function(){
        this.baseMenu = Ext.create('widget.basemenu',{
        	title: this.menuTitle,
        	onExpand: this.onMenuExpand,
        	onCollapse: this.onMenuCollapse
        });
        return this.baseMenu;
    },
    
    /**
     * Restore properties of the component (Last state saved for StateListeners). to restore the state, all the elements must be created
     */
    restoreState: function(){
    	this.baseMenu.restoreState();
    }
});

