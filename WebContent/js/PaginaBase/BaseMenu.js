
/**
 * @class BaseViewer.BaseMenu
 * @extends Ext.panel.Panel
 *
 * A container class for showing a series groups of menus
 * 
 * @constructor
 * Create a new Docked Base Menu
 * @param {Object} config The config object
 */
Ext.define('BaseViewer.BaseMenu', {
    
    extend: 'Ext.panel.Panel',
    alias: 'widget.basemenu',
    viewPrefixId: 'BaseGroupOption-',
    statePrefixId: 'state-',
    id: 'west-panel',
    onExpand:function(){return;},
    onCollapse:function(){return;},
    
    listeners:{
    	expand:function(panel, eOpts){
    		this.onExpand();
    	},
    	collapse:function(panel, eOpts){
    		this.onCollapse();
    	}
    },
    
    initComponent: function() {
    	
    	Ext.apply(this,{
    		region: 'west',
    	    //stateId: 'navigation-panel',
    	    title: this.title,
    	    split: true,
    	    width: 200,
    	    minWidth: 175,
    	    maxWidth: 400,
    	    collapsible: false,
    	    animCollapse: true,
    	    margins: '0 0 0 5',
    	    layout: 'accordion'
    	});
    	
    	if(this.collapsed){
    		new Ext.util.DelayedTask().delay(100,function(){this.onCollapse();},this);
    	}else{
    		new Ext.util.DelayedTask().delay(100,function(){this.onExpand();},this);
    	}

        this.callParent();
    },
    

    /**
     * Add a new Group Menu
     * @param {String} groupId The id of the group menu
     * @param {String} title The title of the group menu
     * @param {String} iconCls The CSS Class for the icon of the group
     * @param {String} optCls The CSS Class for the options
     * @param {String} optItemSelector The item selector sentence to identify the options
     * @param {String} optOverItemCls The CSS Class using for hover the element
     * @param {String} optTpl The String template to construct the element
     */
    addGroupMenu: function(paramObj){
    	if(!paramObj.groupId){
    		//Ext.Msg.alert('Error','"groupId" is missing');
    		Ext.Error.raise({
                msg: '"groupId" is missing',
                option: paramObj,   // whatever was passed into the method
                obj: this
                //'error code': 100 // other arbitrary info
            });
    		return;
    	} 
    	if(!paramObj.title){
    		//Ext.Msg.alert('Error','"title" is missing');
    		Ext.Error.raise({
                msg: '"title" is missing',
                option: paramObj,   // whatever was passed into the method
                obj: this
                //'error code': 100 // other arbitrary info
            });
    		return;
    	}
    	
    	if(!paramObj.iconCls){
    		Ext.apply(paramObj,{iconCls:''});
    	}
    	
    	if(!paramObj.optCls){
    		Ext.apply(paramObj,{optCls:''});
    	}
    	
    	if(!paramObj.optItemSelector){
    		Ext.apply(paramObj,{optItemSelector:''});
    	}
    	
    	if(!paramObj.optOverItemCls){
    		Ext.apply(paramObj,{optOverItemCls:''});
    	}
    	
    	if(!paramObj.optTpl){
    		//Ext.Msg.alert('Error','"optTpl" is missing');
    		Ext.Error.raise({
                msg: '"optTpl" is missing',
                option: paramObj,   // whatever was passed into the method
                obj: this
            });
    		return;
    	}
    	
    	this.add({
    		title: paramObj.title,
    		id: paramObj.groupId,
    		stateId: this.statePrefixId+ paramObj.groupId,
    	    stateful: true,
    	    /*stateEvents: ['collapse','expand'],
    	    listeners:{
    	    	beforestatesave: function(thisState, objState, eOpts){
    	    		Ext.Msg.alert('','1Save State');
    	    		return false;
    	    	},
    	    	beforestaterestore: function(thisState, objState, eOpts){
    	    		Ext.Msg.alert('','Restored State');
    	    		return true;
    	    	}
    	    },*/
    		iconCls: paramObj.iconCls,
    		layout: 'anchor'
    	});
    	
    	
    	Ext.getCmp(paramObj.groupId).add(Ext.create('BaseViewer.BaseGroupOption',{
    		id: this.viewPrefixId + paramObj.groupId,
    		stateId: 'state-' + paramObj.groupId,
    		//stateful: true,
    		//stateEvents: ['itemclick'],
    		groupId: paramObj.groupId,
    		cls: paramObj.optCls,
            itemSelector: paramObj.optItemSelector,
            overItemCls: paramObj.optOverItemCls,
            tpl: paramObj.optTpl
    	}));
    },
    
    
    /**
     * Add a new option Menu to the current group
     * @param {String} groupId The id of the group menu
     * @param {String} title The title of the option
     * @param {String} url The url of the option
     * @param {String} css The css class for this option
     */
    addMenu: function(paramObj){
    	if(!paramObj.groupId){
    		Ext.Msg.alert('Error','addMenu - "groupId" is missing');
    		return;
    	}
    	
    	if(!paramObj.title){
    		Ext.Msg.alert('Error','addMenu - "title" is missing');
    		return;
    	}
    	
    	if(!paramObj.url){
    		Ext.Msg.alert('Error','addMenu - "url" is missing');
    		return;
    	}
    	
    	if(!paramObj.css){
    		Ext.apply(paramObj,{cls:''});
    	}
    	
    	//var storeCmp = this.getComponent(this.viewPrefixId+groupId);
    	var viewCmp = Ext.getCmp(this.viewPrefixId + paramObj.groupId);
    	var storeId = viewCmp.storeId;
    	
    	Ext.getStore(storeId).add({
    		title: paramObj.title, 
    		url: paramObj.url, 
    		css: paramObj.css
    	}); 
    },
    
    /**
     * Restore properties of the component (Last state saved for StateListeners). to restore the state, all the elements must be created
     */
    restoreState: function(){
    	this.applyState(Ext.state.Manager.get(this.statePrefixId + this.id));
    }
});
