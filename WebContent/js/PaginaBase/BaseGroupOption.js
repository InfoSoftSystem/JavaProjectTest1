
/**
 * @class BaseViewer.BaseGroupOption
 * @extends Ext.view.View
 *
 * Class for showing option groups and its options 
 * 
 * @constructor
 * Create a new Group Option
 * @param {Object} config The config object
 */
Ext.define('BaseViewer.BaseGroupOption', {
    
    extend: 'Ext.view.View',
    alias: 'widget.basegroupoption',
    groupId: 0,
    
    /**
     * Id of the Store
     */
    storeId:'',
    
    

    initComponent: function() {
    	var constIdStore = 'baseGroupOptionStore-';
    	
        Ext.apply(this,{
        	storeId: constIdStore + this.groupId,
        	store: Ext.create('Ext.data.Store', {
                id: constIdStore + this.groupId,
            	model: 'OptionGroup' 
            }),
            selModel: {
                mode: 'SINGLE'/* ,
                listeners: {
                    scope: this,
                    //selectionchange: this.onSelectionChange
                    itemclick: this.itemclick
                }*/
            },
            listeners: {
                scope: this,
                itemclick: this.itemclick
                /*contextmenu: this.onContextMenu,
                viewready: this.onViewReady*/
            },
            trackOver: true,
            cls: this.cls,
            itemSelector: this.itemSelector,
            overItemCls: this.overItemCls,
            tpl: this.tpl
        });
        
        this.callParent();
    },
    
    /**
     * Add a new option to the group
     * @param {String} title The title of the option
     * @param {String} url The url of the option
     * @return {BaseViewer.BaseOptions} this object of class BaseOptions
     * /
    addOption: function(paramObj){
    	if(!paramObj.title){
    		Ext.Msg.alert('Error','addOption - "title" is missing');
    		return;
    	}
    	if(!paramObj.url){
    		Ext.Msg.alert('Error','addOption - "url" is missing');
    		return;
    	}
    	var store = Ext.getStore(this.storeId);
    	
    	store.add({
    		title: paramObj.title,
    		url: paramObj.url
    	});
    	
    	return this;
    },/**/
    
    
    /**
     * Fires when an item is clicked
     * @private
     * @param {Ext.view.View} this
     * @param {Ext.data.Model} record The record that belongs to the item
     * @param {HTMLElement} item The item's element
     * @param {Number} index The item's index
     * @param {Ext.EventObject} e The raw event object
     * @param {Object} eOpts The options object passed to Ext.util.Observable.addListener.
     */
    itemclick: function(thisObj, record, item, index, e, eOpts){
        //Ext.Msg.alert('PRUEBAOPTION',record.get('url'));
        location.href = record.get('url');
    }
});
