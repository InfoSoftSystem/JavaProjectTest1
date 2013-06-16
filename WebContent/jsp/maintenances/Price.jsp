<%@ page language="java" contentType="text/html; charset=ISO-8859-1" pageEncoding="ISO-8859-1"%>
<%@ taglib uri="/WEB-INF/struts-logic.tld" prefix="logic"%>
<%@ taglib uri="/WEB-INF/struts-bean.tld" prefix="bean"%>
<%@ taglib uri="/WEB-INF/struts-html.tld" prefix="html"%>
<%@ taglib uri="/WEB-INF/struts-tiles.tld" prefix="tiles"%>

<%@ page import="org.promefrut.simefrut.struts.administration.forms.UserForm"%>

<tiles:insert name="plantillaBase" definition="plantillaBase">
	<tiles:put name="base">
		<html:base />
	</tiles:put>
	
	<tiles:put name="help" type="String">
	</tiles:put>
		
	<tiles:put name="scripts" type="String">
		<script>
			
			/**
				This funtion is used to fire the events for positioning the components. centerNow function is used on the properties
				onMenuCollapse and onMenuExpand of BaseViewer.BaseView. This function must be in every page if the positioning is necesary
				
			*/
			function centerNow(){
				Ext.getCmp('formulario').fireEvent('position',{"this":Ext.getCmp('formulario'), eOpts: {}});
				Ext.getCmp('GridPanel').fireEvent('position',{"this":Ext.getCmp('GridPanel'), eOpts: {}});
			}
			
			var globalProdDesc = '';
			//Create a delay recurrent task for assigning Product Type
			var taskPtypeId = new Ext.util.DelayedTask(function(){
				
				if(Ext.getCmp('prodId').getRawValue() != globalProdDesc){
					
					if(Ext.getCmp('prodId').getRawValue() != ''){
						taskFilterTunitSk.delay(200);
					}
					
					globalProdDesc= Ext.getCmp('prodId').getRawValue();
				}
				
				taskPtypeId.delay(1000);
			});
			
			taskPtypeId.delay(4000); 
			
			//Create a delay task for assigning Traditional Unit
			var taskFilterTunitSk = new Ext.util.DelayedTask(function(){
				dofilterTunitSk();
			});
			
			//Every time a parent field changes, will activate the count down, if more than 1 parent fields calls simultaneusly (Like Grid-- >to form) 
			// the Delayed task will call the Traditional unit assignment just 1
			function filterTunitSk(){
				taskFilterTunitSk.delay(200);
			}
			
			//This function set the value and filter the elements for Traditional unit ComboBox
			function dofilterTunitSk(){
				var tunitSk = Ext.getCmp('tunitSk');
				var strTunitSk = tunitSk.store;
				var strProd = Ext.getCmp('prodId').getStore();
				
				var tmpValueTunitSk = tunitSk.getValue();
    			tunitSk.clearValue();
    			
    			strTunitSk.suspendEvents();
        		strTunitSk.clearFilter();
        		strTunitSk.resumeEvents();
        		
				strProd.filter([
					{id: 'prodDesc', property: 'prodDesc', value: Ext.getCmp('prodId').getRawValue(), exactMatch: true}
				]);
				
				var ptypeId=0;
				if(strProd.getCount()>0){
					ptypeId = strProd.getAt(0).get('ptypeId');
				}
				
				Ext.getCmp('ptypeId').setValue(ptypeId);
				strProd.clearFilter();
				
        		strTunitSk.filter([
        		    {id: 'ctrId', property: 'ctrId', value: getCtrId(), exactMatch: true},
        			{id: 'prodId', property: 'prodId', value: Ext.getCmp('prodId').getValue(), exactMatch: true},
        			{id: 'sizeId', property: 'sizeId', value: getSizeId(), exactMatch: true}, 
					{id: 'ptypeId', property: 'ptypeId', value: ptypeId , exactMatch: true}
        		]);
        		
        		var tmp=getPrdVarId();
        		
        		if(!Ext.isEmpty(tmp)){
        			strTunitSk.filter([
						{id: 'prdVarId', property: 'prdVarId', value: tmp, exactMatch: true}
        			]);
        		}
        		//,
        		
        		tunitSk.setValue(tmpValueTunitSk);
			}
			
			/**
			* Function that return current ctrId from ComboBox ctrSk
			*/
			function getCtrId(){
				var tmpCmp = Ext.getCmp('ctrSkCmb');
				var tmpStore = tmpCmp.store;
				var tmpValue = tmpCmp.getValue();
				
				var tmpRecord = tmpStore.getById(tmpValue);
				var result = '';
				
				if(!Ext.isEmpty(tmpRecord)){
					result = tmpRecord.get('ctrId');
				}
        		return result;
			}
			
			/**
			* Function that return current varId from ComboBox prdVarSk
			*/
			function getPrdVarId(){
				var tmpCmp = Ext.getCmp('prdVarSk');
				var tmpStore = tmpCmp.store;
				var tmpValue = tmpCmp.getValue();
				
				var tmpRecord = tmpStore.getById(tmpValue);
				var result = '';
				
				if(!Ext.isEmpty(tmpRecord)){
					result = tmpRecord.get('varId');
				}
				return result;
			}
			
			/**
			* Function that return current sizeId from ComboBox sizeSk
			*/
			function getSizeId(){
				var tmpCmp = Ext.getCmp('sizeSk');
				var tmpStore = tmpCmp.store;
				var tmpValue = tmpCmp.getValue();
				
				var tmpRecord = tmpStore.getById(tmpValue);
				var result = '';
				
				if(!Ext.isEmpty(tmpRecord)){
					result = tmpRecord.get('sizeId');
				}
				return result;
			}
			
			var globalRangeValidator = false;
			var globalAccion = '';
			
			function validateRange(){
				if(globalRangeValidator){
           			globalAccion = '';
           			globalRangeValidator = false;
           			return true;
           		}else{
           			var tmpInf;
               		var porcent = 0.3;
               		var tmpMsg='';
               		var tmpPermi;
               		var value; 
               		
					tmpInf = Ext.getCmp('priceInfUni').getValue();
               		value = Ext.getCmp('priceSupUni').getValue();
               		
               		tmpPermi = Math.round(tmpInf*(1+porcent)*100)/100;
               		
               		if(value > tmpPermi){
               			tmpMsg = tmpMsg+'<bean:message key="price.priceSupUni.outOfRange"/>'.replace(/\[0\]/g,tmpPermi).replace(/\[1\]/g,(porcent*100)+'%');
               		}
               		
               		tmpInf = Ext.getCmp('priceInfKilo').getValue();
               		value = Ext.getCmp('priceSupKilo').getValue();
               		
               		tmpPermi = Math.round(tmpInf*(1+porcent)*100)/100;
               		
               		if(value > tmpPermi){
               			tmpMsg = tmpMsg+'<br/><bean:message key="price.priceSupKilo.outOfRange"/>'.replace(/\[0\]/g,tmpPermi).replace(/\[1\]/g,(porcent*100)+'%');
               		}
               		
               		if(tmpMsg==''){
               			return true;
               		}else{
               			tmpMsg = tmpMsg+'<br/><bean:message key="global.warning.confirmation"/>';
               			
	               		Ext.Msg.show({
	           				title: '<bean:message key="global.warning.title" />',
	           				msg: tmpMsg,
	           				scope:this,
	           				icon: Ext.Msg.WARNING,
	           				buttons: Ext.Msg.YESNO,
	           				modal: true,
	           				fn: function(botton){
	           					if(botton =='yes'){
	           						globalRangeValidator = true;
	           						
	           						if(globalAccion=='insert'){
	    	       						Ext.getCmp(idFormulario+'-btnInsert').fireEvent('click');
	           						}else if(globalAccion=='update'){
	           							Ext.getCmp(idFormulario+'-btnUpdate').fireEvent('click');
	           						}
	           					}
	           				},
	           				animateTarget: 'globalWarnings'
	           			});
               		}
           			return globalRangeValidator;
           		}
			}
			
			function validateRangeIns(){
				globalAccion = 'insert';
				return validateRange();
			}
			
			function validateRangeUpd(){
				globalAccion = 'update';
				return validateRange();
			}
			
			var idFormulario = 'formulario';
			
			
			Ext.onReady(function(){
				//Property to disable the country field if the user is not an Administrator	
				var ctrDisabled;
				<%
		        	UserForm user = (UserForm) session.getAttribute("user");
		            if(user.getNoCountry()){
		        %>
		        	ctrDisabled=false;
		        <%	}else{%>
		        	ctrDisabled= true;
		        <%	}%>
				
		        //Property used to choose the country column according to current language 
		        var ctrMapping = javaLocation=='es'?'ctrdescspa':'ctrdesceng';
		        var userCtrId = <%=user.getCtrId()%>;
		        
				//************************************************************************************************
				//*** Models for Forms
				
				Ext.define('modelCtrSk', {
				    extend: 'Ext.data.Model',
				    fields: [
				        {name: 'ctrSk', type: 'int', mapping:'ctrsk'},
				        {name: 'ctrId', type: 'numeric', mapping: 'ctrid'},
				        {name: 'ctrDesc', type: 'string', mapping: ctrMapping}
				    ],
				    idProperty: 'ctrSk'
				});
				
				Ext.define('modelVarSk', {
				    extend: 'Ext.data.Model',
				    fields: [
				        {name: 'prdVarSk', type: 'int', mapping:'prdvarsk'},
				        {name: 'prodId',  type: 'int', mapping:'prodid'},
				        //{name: 'prodDesc', type: 'string', mapping:'proddesc'},
				        {name: 'varDesc',  type: 'string', mapping:'vardesc'},
				        {name: 'varId',  type: 'int', mapping:'varid'}
				    ],
				    idProperty: 'prdVarSk'
				});
				
				Ext.define('modelProdId', {
				    extend: 'Ext.data.Model',
				    fields: [
				        {name: 'prodId', type: 'int', mapping:'prodid'},
						{name: 'ptypeId', type: 'int', mapping:'ptypeid'},
				        {name: 'prodDesc', type: 'string', mapping:'proddesc'}
				    ]/*,
				    idProperty: 'prodId'*/
				});
				
				Ext.define('modelQuaSk', {
					extend: 'Ext.data.Model',
					fields: [
				        {name: 'quaSk', type: 'int', mapping:'quask'},
						{name: 'quaDesc', type: 'string', mapping: 'quadesc'}
				    ],
		            idProperty: 'quaSk'
				});
				
				Ext.define('modelSizeSk', {
					extend: 'Ext.data.Model',
					fields: [
				        {name: 'sizeSk', type: 'int', mapping:'sizesk'},
				        {name: 'sizeId', type: 'int', mapping:'sizeid'},
				        {name: 'sizeDesc', type: 'string', mapping: 'sizedesc'}
				    ],
		            idProperty: 'sizeSk'
				});

				Ext.define('modelClevSk', {
					extend: 'Ext.data.Model',
					fields: [
				        {name: 'clevSk', type: 'int', mapping:'clevsk'},
				        {name: 'clevDesc', type: 'string', mapping: 'clevdesc'}
				    ],
		            idProperty: 'clevSk'
				});
				
				Ext.define('modelTunitSk', {
					extend: 'Ext.data.Model',
					fields: [
				        {name: 'tunitSk', type: 'int', mapping:'tunitsk'},
				        {name: 'ctrId', type: 'int', mapping:'ctrid'},
						{name: 'ptypeId', type: 'int', mapping:'ptypeid'},
				        {name: 'prodId', type: 'int', mapping:'prodid'},
				        {name: 'prdVarId', type: 'int', mapping:'prdvarid'},
				        {name: 'sizeId', type: 'int', mapping:'sizeid'},
				        {name: 'tunitDesc', type: 'string', mapping: 'tunitdesc'},
						{name: 'tunitKilo', type: 'numeric', mapping: 'tunitkilo'}
				    ],
		            idProperty: 'tunitSk'
				});
				
				Ext.define('modelOriCtrId', {
					extend: 'Ext.data.Model',
					fields: [
				        {name: 'oriCtrId', type: 'numeric', mapping: 'orictrid'},
				        {name: 'oriCtrDesc', type: 'string', mapping: ctrMapping}
				    ],
		            idProperty: 'oriCtrId'
				});
				
				Ext.define('modelOriRegId', {
					extend: 'Ext.data.Model',
					fields: [
			        	{name: 'oriRegId', type: 'numeric', mapping: 'oriregid'},
						{name: 'oriCtrId', type: 'numeric', mapping: 'orictrid'},
				        {name: 'oriRegDesc', type: 'string', mapping: 'oriregdesc'}
				    ],
		            idProperty: 'oriRegId'
				});
				
				Ext.define('modelOriProvId', {
					extend: 'Ext.data.Model',
					fields: [
						{name: 'oriProvId', type: 'numeric', mapping: 'oriprovid'},
			        	{name: 'oriRegId', type: 'numeric', mapping: 'oriregid'},
				        {name: 'oriProvDesc', type: 'string', mapping: 'oriprovdesc'}
				    ],
		            idProperty: 'oriProvId'
				});
				
				Ext.define('modelPtypeId', {
					extend: 'Ext.data.Model',
					fields: [
				        {name: 'ptypeId', type: 'int', mapping:'ptypeid'},
						{name: 'prodId', type: 'int', mapping:'prodid'},
				        {name: 'ptypeDesc', type: 'string', mapping: 'ptypedesc'}
				    ],
		            idProperty: 'ptypeId'
				});
	            
				
				Ext.create('Ext.container.Container',{
                    //xtype: 'container',
                    renderTo:'container',
                    layout: 'hbox',
                    defaultType: 'textfield',
                    defaults:{
                    	labelAlign:'right',
                    	margin: '20 0 4 0'
                    },
                    items: [{
      	        		xtype:'container',html:'<br/>'
      	        	},{
	      	              	xtype: 'combo',
	      	                fieldLabel: '<bean:message key="price.ctrSk"/>',
	      	                id:'ctrSkCmb',
	      	                allowBlank:true,
	      	            	disabled: false,//ctrDisabled,
					        store: Ext.create('Ext.data.Store',{
					        	model: 'modelCtrSk',
					        	data: {registers: <%=request.getAttribute("countriesCollection")%>},
					        	proxy:{
					        		type: 'memory',
					        		reader: {
					                    type: 'json',
					                    root: 'registers'
					                }
					        	},
	      			        	sorters: [{
	      				            property: 'ctrDesc',
	      				            direction: 'ASC'
	      				        }]
					        }),
	      	              	lastQuery:'',
					        queryMode: 'local',
					        displayField: 'ctrDesc',
					        valueField: 'ctrSk',
					        //editable: false,
					        forceSelection: true,
					        emptyText: '<bean:message key="combo.emptyText"/>',
					        listeners:{
					        	afterrender:function(){
					        		var tmpStore = this.store;
					        		
					        		tmpStore.suspendEvents();
					        		tmpStore.clearFilter();
					        		tmpStore.resumeEvents();
					        		
					        		tmpStore.filter([{
    					            	id: 'ctrId',
					        			property: 'ctrId',
					        			exactMatch: true,
    					            	value: userCtrId
    					            }]);

					        		//Now we set user's country as default value in the combobox
					        		var tmpSk = tmpStore.getAt(0).get('ctrSk');
					        		tmpStore.suspendEvents();
					        		tmpStore.clearFilter();
					        		tmpStore.resumeEvents();
					        		
					        		this.setValue(tmpSk);
					        		new Ext.util.DelayedTask().delay(300,function(){
					        			Ext.getCmp('ctrSk').setValue(tmpSk);
					        		});
					        	},
					        	change: function(field, newValue, oldValue){
					        		if( Ext.isEmpty(oldValue) ){
										new Ext.util.DelayedTask().delay(300,function(){
											Ext.getCmp('ctrSk').setValue(field.getValue());
										});
									}
									
									filterTunitSk();
					        	}, 
								select: function(combo){
									Ext.getCmp('ctrSk').setValue(Ext.getCmp('ctrSkCmb').getValue());
									Ext.getCmp(idFormulario+'-btnCancel').fireEvent('click');
									//filterTunitSk();
								}
					        }
	      	            },{
							xtype: 'datefield',
							fieldLabel: '<bean:message key="price.dateField"/>',
							id: 'dateFieldFiltro',
							format: 'd/m/Y',
							maxValue: new Date(),  // limited to the current date or prior
							value: Ext.Date.add(new Date(), Ext.Date.DAY,-1),
							listeners:{
								render: function(){
									try{
										Ext.getCmp('dateField').setValue(Ext.getCmp('dateFieldFiltro').getRawValue());
									}catch(e){
										;
									}
								},
								afterrender:function(){
									new Ext.util.DelayedTask().delay(300,function(){
										Ext.getCmp('dateField').setValue(Ext.getCmp('dateFieldFiltro').getRawValue());
					        		});
								},
								change: function(field, newValue, oldValue){
									if( !Ext.isEmpty(oldValue) ){
										Ext.getCmp('dateField').setValue(field.getRawValue());
										
										Ext.Msg.show({
											title: '<bean:message key="global.warning.title" />',
											msg: '<bean:message key="price.dateCopy"/>',
											icon: Ext.Msg.WARNING,
											buttons: Ext.Msg.YESNO,
											modal: true,
											//closable: false,
											fn: function(botton){
												if(botton =='yes'){
													Ext.getCmp('accion').setValue("copyData");
													Ext.getCmp('formulario')._doCustomSubmit();
													
													/*new Ext.util.DelayedTask().delay(2000, function(){
														Ext.getStore('dataStore').proxy.extraParams={
															ctrSk: Ext.getCmp('ctrSk').getValue(),
															dateField: Ext.getCmp('dateField').getRawValue()
														};
														Ext.getStore('dataStore').load();
													})*/
												}else if(botton =='no' || botton =='cancel'){
													Ext.getCmp(idFormulario+'-btnCancel').fireEvent('click');
												}
											},
											animateTarget: 'globalWarnings'
										});
									}else{
										new Ext.util.DelayedTask().delay(500,function(){
											Ext.getCmp('dateField').setValue(field.getRawValue());
										});
									}
								}
							}
	      	            }
                    ]
  	        	});
				
				//************************************************************************************
				var globalMargin = '0 75 5 0';
				Ext.create('Ext.custom.FormPanel',{
					id: idFormulario, 
					url:'priceAction.do',
					standardSubmit: false,
					habBtnDelete: true,
					labelWidth: 100,
					width: 650,
					title: '<bean:message key="price.title" />',
					insertFn: validateRangeIns,
					updateFn: validateRangeUpd,
					onFailure: function(){
						Ext.getCmp('dateField').setValue(Ext.getCmp('dateFieldFiltro').getRawValue());
						Ext.getCmp('ctrSk').setValue(Ext.getCmp('ctrSkCmb').getValue());
						Ext.getStore('dataStore').proxy.extraParams={
   							ctrSk: Ext.getCmp('ctrSk').getValue(),
			                dateField: Ext.getCmp('dateField').getRawValue()
			            };
						Ext.getStore('dataStore').load();
					},
					cancelFn: function(){
						Ext.getCmp('dateField').setValue(Ext.getCmp('dateFieldFiltro').getRawValue());
						Ext.getCmp('ctrSk').setValue(Ext.getCmp('ctrSkCmb').getValue());
						Ext.getStore('dataStore').proxy.extraParams={
   							ctrSk: Ext.getCmp('ctrSk').getValue(),
			                dateField: Ext.getCmp('dateField').getRawValue()
			            };
						//var m = wait();
						Ext.getStore('dataStore').load(function(records, operation, success) {
							/*alert(success);
							console.log(records);
							
							if(success){
							
							}else{
							
								Ext.get('globalErrors').update(action.result.msg);
								showGlobalErrors();
							}*/
						});
						return true;
					},
					afterCancelFn: function(){
						//Set default values
						Ext.getCmp('dateField').setValue(Ext.getCmp('dateFieldFiltro').getRawValue());
						Ext.getCmp('ctrSk').setValue(Ext.getCmp('ctrSkCmb').getValue());
						Ext.getCmp('clevSk').fireEvent('afterrender');
						Ext.getCmp('oriCtrId').fireEvent('afterrender');
					},
					items: [{
	      	            xtype:'fieldset',
	      	            checkboxToggle:false,
	      	            //title: 'User Information',
	      	            defaultType: 'textfield',
	      	            collapsed: false,
	      	            collapsible:true,
	      	            layout: 'anchor',
	      	            defaults: {
	      	                anchor: '100%'
	      	            },
	      	            items :[
						{
	      	        		xtype:'hidden',
	      	        		id:'priceId',
	      	        		name:'priceId'
	      	        	},{
	                		xtype:'hidden',
	                		id:'ctrSk',
	                		name:'ctrSk'
                		},{
	                		xtype:'hidden',
	                		id:'dateField',
	                		name:'dateField'
                		},/*{
	                		xtype:'hidden',
	                		id:'ptypeId',
	                		name:'ptypeId'
                		},*/{
	      	              	xtype: 'combo',
	      	                fieldLabel: 'Ptype',
	      	                id:'ptypeId',
							name:'ptypeId',
	      	                allowBlank:true,
	      	            	hidden: true,
					        store: Ext.create('Ext.data.Store',{
					        	model: 'modelPtypeId',
					        	data: {registers: <%=request.getAttribute("productTypesCollection")%>},
					        	proxy:{
					        		type: 'memory',
					        		reader: {
					                    type: 'json',
					                    root: 'registers'
					                }
					        	},
	      			        	sorters: [{
	      				            property: 'ptypeDesc',
	      				            direction: 'ASC'
	      				        }]
					        }),
	      	              	lastQuery:'',
					        queryMode: 'local',
					        displayField: 'ptypeDesc',
					        valueField: 'ptypeId',
					        //editable: false,
					        forceSelection: true,
					        emptyText: '<bean:message key="combo.emptyText"/>',
					        listeners:{
					        	change: function(field, newValue, oldValue){
									if(newValue!=0){
										new Ext.util.DelayedTask().delay(100,function(){
											var tmpPtype = Ext.getCmp('ptypeId');
											tmpPtype.getStore().clearFilter();
											tmpPtype.getStore().filter([
												{id: 'ptypeId', property: 'ptypeId', value: tmpPtype.getValue(), exactMatch: true}
											]);
											
											if(tmpPtype.getStore().getCount()>0){
												var tmpProd = Ext.getCmp('prodId');
												tmpProd.setValue(tmpProd.findRecord('ptypeId',tmpPtype.getValue()));
											}
											tmpPtype.getStore().clearFilter();
										});
									}
					        	}
					        }
	      	            },{
	                        xtype: 'container',
	                        layout: 'hbox',
	                        defaultType: 'textfield',
	                        defaults:{
	                        	labelAlign:'right',
	                        	margin: globalMargin
	                        },
	                        items: [{
	    	      	              	xtype: 'combo',
	    	      	                fieldLabel: '<bean:message key="price.prodId"/>',
	    	      	                id:'prodId',
	    	      	                name: 'prodId',
	    	      	              	allowBlank:false,
	    					        store: 	Ext.create('Ext.data.Store',{
	    					        	model: 'modelProdId',
	    					        	data: {registers: <%=request.getAttribute("productsCollection")%>},
	    					        	proxy:{
	    					        		type: 'memory',
	    					        		reader: {
	    					                    type: 'json',
	    					                    root: 'registers'
	    					                }
	    					        	},
	    	      			        	sorters: [{
	    	      				            property: 'prodId',
	    	      				            direction: 'ASC'
	    	      				        }]
	    					        }),
	    					        queryMode: 'local',
	    					        displayField: 'prodDesc',
	    					        valueField: 'prodId',
	    					        editable: true,
	    					        forceSelection: true,
	    					        emptyText: '<bean:message key="combo.emptyText"/>',
	    					        listeners:{
	    					        	buffer: 50,
	    					        	// delete the previous query in the beforequery event or set
	    					            // combo.lastQuery = null (this will reload the store the next time it expands)
	    					            beforequery: function(qe){
	    					                delete qe.combo.lastQuery;
	    					            },
	    					        	change: function(combo, newValue, oldValue){
	    					        		var varSk = Ext.getCmp('prdVarSk');
	    					        		var strVarSk = varSk.store;
	    					        		
	    					        		varSk.enable();
    					        			var tmpValue = varSk.getValue();
    					        			varSk.clearValue();
	    					        		
    					        			
    					        			strVarSk.suspendEvents();
	    					        		strVarSk.clearFilter();
	    					        		strVarSk.resumeEvents();
	    					        		
	    					        		/*strVarSk.filterBy(function(record){
	    					        			var v1 = new Number(record.get('prodId'));
	    					        			var v2 = new Number(combo.getValue());
	    					        			if( v1.valueOf()== v2.valueOf()){
	    					        				return true;
	    					        			}else{
	    					        				return false;
	    					        			}
	    					        		}, this);
	    					        		/**/
	    					        		
	    					        		strVarSk.filter([
	    					        			{id: 'prodId', property: 'prodId', value: combo.getValue(), exactMatch: true}
	    					        			/*{fn: function(record){
	    						        			var v1 = new Number(record.get('prodId'));
	    						        			var v2 = new Number(combo.getValue());
	    						        			if( v1.valueOf()== v2.valueOf()){
	    						        				return true;
	    						        			}else{
	    						        				return false;
	    						        			}
	    						        		}}*/
	    					        		]);
	    					        		/**/
	    					        		
	    					        		varSk.setValue(tmpValue);
	    					        		
	    					        		filterTunitSk();
	    					        	}
	    					        }
	    	      	            },{
	    	      	              	xtype: 'combo',
	    	      	                fieldLabel: '<bean:message key="price.varId"/>',
	    	      	                id:'prdVarSk',
	    	      	                name: 'prdVarSk',
	    	      	              	allowBlank:true,
	    	      	              	disabled: true,
	    	      	              	lastQuery:'', //! IMPORTANT <-- if not set to empty, the first filter doesn't work!
	    	      	              	store: 	Ext.create('Ext.data.Store',{
	    	      			        	model: 'modelVarSk',
	    	      			        	data: {registers: <%=request.getAttribute("varietiesCollection")%>},
	    	      			        	proxy:{
	    	      			        		type: 'memory',
	    	      			        		reader: {
	    	      			                    type: 'json',
	    	      			                    root: 'registers'
	    	      			                }
	    	      			        	},
	    	      			        	sorters: [{
	    	      				            property: 'prdVarSk',
	    	      				            direction: 'ASC'
	    	      				        }]
	    	      			        }),
	    					        queryMode: 'local',
	    					        displayField: 'varDesc',
	    					        valueField: 'prdVarSk',
	    					        editable: false,
	    					        forceSelection: true,
	    					        emptyText: '<bean:message key="combo.emptyText"/>',
	    					        listeners:{
	    					        	buffer: 80,
	    					        	specialkey: function(field, e){
	    				                    // e.HOME, e.END, e.PAGE_UP, e.PAGE_DOWN,
	    				                    // e.TAB, e.ESC, arrow keys: e.LEFT, e.RIGHT, e.UP, e.DOWN
	    				                    if (e.getKey() == e.BACKSPACE) {
	    				                        if(Ext.isEmpty(field.getValue())){
	    				                    		new Ext.util.DelayedTask().delay(100,function(){
	    				                    			Ext.getCmp('prodId').fireEvent('change', Ext.getCmp('prodId'));
	    				                    		});
	    				                        }
	    				                    }
	    				                },
										change: filterTunitSk
	    					        }
	    	      	            }]//items Container
	      	        	},{
	                        xtype: 'container',
	                        layout: 'hbox',
	                        defaultType: 'textfield',
	                        defaults:{
	                        	labelAlign:'right',
	                        	margin: globalMargin
	                        },
	                        items: [{
    	      	              	xtype: 'combo',
    	      	                fieldLabel: '<bean:message key="price.quaSk"/>',
    	      	                id:'quaSk',
    	      	                name: 'quaSk',
    	      	              	allowBlank:false,
    					        store: Ext.create('Ext.data.Store',{
    	      			        	model: 'modelQuaSk',
    	      			        	data: {registers: <%=request.getAttribute("qualitiesCollection")%>},
    	      			        	proxy:{
    	      			        		type: 'memory',
    	      			        		reader: {
    	      			                    type: 'json',
    	      			                    root: 'registers'
    	      			                }
    	      			        	},
    	      			        	sorters: [{
    	      				            property: 'quaSk',
    	      				            direction: 'ASC'
    	      				        }]
    	      			        }), 
    	      	              	lastQuery:'',
    					        queryMode: 'local',
    					        displayField: 'quaDesc',
    					        valueField: 'quaSk',
    					        editable: true,
    					        forceSelection: true,
    					        emptyText: '<bean:message key="combo.emptyText"/>'
    					    },{
    	      	              	xtype: 'combo',
    	      	                fieldLabel: '<bean:message key="price.sizeSk"/>',
    	      	                id:'sizeSk',
    	      	                name: 'sizeSk',
    	      	              	allowBlank:false,
    					        store: Ext.create('Ext.data.Store',{
    	      			        	model: 'modelSizeSk',
    	      			        	data: {registers: <%=request.getAttribute("sizesCollection")%>},
    	      			        	proxy:{
    	      			        		type: 'memory',
    	      			        		reader: {
    	      			                    type: 'json',
    	      			                    root: 'registers'
    	      			                }
    	      			        	},
    	      			        	sorters: [{
    	      				            property: 'sizeDesc',
    	      				            direction: 'DESC'
    	      				        }]
    	      			        }),
    	      	              	lastQuery:'',
    					        queryMode: 'local',
    					        displayField: 'sizeDesc',
    					        valueField: 'sizeSk',
    					        editable: true,
    					        forceSelection: true,
    					        emptyText: '<bean:message key="combo.emptyText"/>',
    					        listeners:{
    					        	change: filterTunitSk
    					        }
    					    }]
	                    },{
	                        xtype: 'container',
	                        layout: 'hbox',
	                        defaultType: 'textfield',
	                        defaults:{
	                        	labelAlign:'right',
	                        	margin: globalMargin
	                        },
	                        items: [{
    	      	              	xtype: 'combo',
    	      	                fieldLabel: '<bean:message key="price.clevSk"/>',
    	      	                id:'clevSk',
    	      	                name: 'clevSk',
    	      	              	allowBlank:true,
    	      	              	store: Ext.create('Ext.data.Store',{
    	      			        	model: 'modelClevSk',
    	      			        	data: {registers: <%=request.getAttribute("commLevelsCollection")%>},
    	      			        	proxy:{
    	      			        		type: 'memory',
    	      			        		reader: {
    	      			                    type: 'json',
    	      			                    root: 'registers'
    	      			                }
    	      			        	},
    	      			        	sorters: [{
    	      				            property: 'clevSk',
    	      				            direction: 'ASC'
    	      				        }]
    	      			        }),
    	      	              	lastQuery:'',
    					        queryMode: 'local',
    					        displayField: 'clevDesc',
    					        valueField: 'clevSk',
    					        editable: true,
    					        forceSelection: true,
    					        emptyText: '<bean:message key="combo.emptyText"/>',
    					        listeners:{
    					        	afterrender:function(){
    					        		var tmpStore = this.store;
    					        		
    					        		if(tmpStore.getCount() > 0){
	    					        		//Now we set user's country as default value in the combobox
	    					        		var tmpSk = tmpStore.getAt(0).get('clevSk');
	    					        		this.setValue(tmpSk);
    					        		}
    					        	}
    					        }
    					    },{
    	      	              	xtype: 'combo',
    	      	                fieldLabel: '<bean:message key="price.tunitSk"/>',
    	      	                id:'tunitSk',
    	      	                name: 'tunitSk',
    	      	              	allowBlank:false,
    					        store: Ext.create('Ext.data.Store',{
    	      			        	model: 'modelTunitSk',
    	      			        	data: {registers: <%=request.getAttribute("traditionalUnitsCollection")%>},
    	      			        	proxy:{
    	      			        		type: 'memory',
    	      			        		reader: {
    	      			                    type: 'json',
    	      			                    root: 'registers'
    	      			                }
    	      			        	},
    	      			        	sorters: [{
    	      				            property: 'tunitSk',
    	      				            direction: 'ASC'
    	      				        }]
    	      			        }),
    	      	              	lastQuery:'',
    					        queryMode: 'local',
    					        displayField: 'tunitDesc',
    					        valueField: 'tunitSk',
    					        editable: true,
    					        forceSelection: true,
    					        emptyText: '<bean:message key="combo.emptyText"/>',
    					        listeners:{
    					        	buffer: 80,
    					        	specialkey: function(field, e){
    				                    // e.HOME, e.END, e.PAGE_UP, e.PAGE_DOWN,
    				                    // e.TAB, e.ESC, arrow keys: e.LEFT, e.RIGHT, e.UP, e.DOWN
    				                    if (e.getKey() == e.BACKSPACE) {
    				                        if(Ext.isEmpty(field.getValue())){
    				                    		new Ext.util.DelayedTask().delay(100,function(){
    				                    			Ext.getCmp('prodId').fireEvent('change', Ext.getCmp('prodId'));
    				                    		});
    				                        }
    				                    }
    				                },
									change: function(field, newValue, oldValue){
										var tmpValue = Ext.getCmp('priceInfUni').getValue();
										Ext.getCmp('priceInfUni').fireEvent('change',Ext.getCmp('priceInfUni'), tmpValue, tmpValue);
										
										tmpValue = Ext.getCmp('priceSupUni').getValue();
										Ext.getCmp('priceSupUni').fireEvent('change',Ext.getCmp('priceSupUni'), tmpValue, tmpValue);
									}
    					        }
    					    }]//items
	                    },{
	      	        		xtype:'container',html:'<br/>'
	      	        	},{
	                        xtype: 'container',
	                        layout: 'hbox',
	                        defaultType: 'textfield',
	                        defaults:{
	                        	labelAlign:'right',
	                        	margin: globalMargin
	                        },
	                        items: [{
		      	                xtype: 'numberfield',
	                        	fieldLabel: '<bean:message key="price.priceInfUni"/>',
		      	                id: 'priceInfUni',
		      	                name: 'priceInfUni',
		      	              	minValue: 0,
		      	                allowBlank:false,
		      	            	blankText: '<bean:message key="price.priceInfUni.required" />',
		      	            	hideTrigger: true,
		      	              	keyNavEnabled: false,
		      	              	mouseWheelEnabled: false,
		      	              	validator:function(value){
		      	              		var tmpSup = Ext.getCmp('priceSupUni').getValue();
		      	              		
		      	              		if(value>tmpSup){
		      	              			return '<bean:message key="price.priceInfUni.superior"/>';
		      	              		}
									return true;
		      	              	},
								listeners:{
									change: function(field, newValue, oldValue){
										var tmpCmp = Ext.getCmp('tunitSk');
										var tmpStore = tmpCmp.store;
										var tmpValue = tmpCmp.getValue();
										var priceInfKilo = '';
										
										if(!Ext.isEmpty(newValue)){
											if(!Ext.isEmpty(tmpValue)){
												var tmpRecord = tmpStore.getById(tmpValue);
												var tunitKilo = '';

												if(!Ext.isEmpty(tmpRecord)){
													tunitKilo = tmpRecord.get('tunitKilo');
													
													if(!Ext.isEmpty(tunitKilo)){
														priceInfKilo = (Math.round(newValue/tunitKilo*100)/100);
													}
												}
											}/**/
										}
										Ext.getCmp('priceInfKilo').setValue(priceInfKilo);
									}
								}
		      	            },{
		      	                xtype: 'numberfield',
	                        	fieldLabel: '<bean:message key="price.priceSupUni"/>',
		      	                id: 'priceSupUni',
		      	                name: 'priceSupUni',
		      	              	minValue: 0,
		      	                allowBlank:false,
		      	            	blankText: '<bean:message key="price.priceSupUni.required" />',
		      	            	hideTrigger: true,
		      	              	keyNavEnabled: false,
		      	              	mouseWheelEnabled: false,
		      	              	validator: function(value){
		      	              		Ext.getCmp('priceInfUni').validate();
		      	              		return true;
		      	              	},
								listeners:{
									change: function(field, newValue, oldValue){
										var tmpCmp = Ext.getCmp('tunitSk');
										var tmpStore = tmpCmp.store;
										var tmpValue = tmpCmp.getValue();
										var priceInfKilo = '';
										
										if(!Ext.isEmpty(newValue)){
											if(!Ext.isEmpty(tmpValue)){
												var tmpRecord = tmpStore.getById(tmpValue);
												var tunitKilo = '';

												if(!Ext.isEmpty(tmpRecord)){
													tunitKilo = tmpRecord.get('tunitKilo');
													
													if(!Ext.isEmpty(tunitKilo)){
														priceInfKilo = (Math.round(newValue/tunitKilo*100)/100);
													}
												}
											}/**/
										}
										Ext.getCmp('priceSupKilo').setValue(priceInfKilo);
									}
								}
		      	            }
	                        ]
	                    },{
	                        xtype: 'container',
	                        layout: 'hbox',
	                        defaultType: 'textfield',
	                        defaults:{
	                        	labelAlign:'right',
	                        	margin: globalMargin
	                        },
	                        items: [{
		      	                xtype: 'numberfield',
	                        	fieldLabel: '<bean:message key="price.priceInfKilo"/>',
		      	                id: 'priceInfKilo',
		      	                name: 'priceInfKilo',
		      	              	minValue: 0,
		      	                allowBlank:false,
		      	            	blankText: '<bean:message key="price.priceInfKilo.required" />',
		      	            	hideTrigger: true,
								editable: false,
		      	              	keyNavEnabled: false,
		      	              	mouseWheelEnabled: false,
		      	              	validator:function(value){
		      	              		var tmpSup = Ext.getCmp('priceSupKilo').getValue();
		      	              		
		      	              		if(value>tmpSup){
		      	              			return '<bean:message key="price.priceInfKilo.superior"/>';
		      	              		}
		      	              		return true;
		      	              	}
		      	            },{
		      	                xtype: 'numberfield',
	                        	fieldLabel: '<bean:message key="price.priceSupKilo"/>',
		      	                id: 'priceSupKilo',
		      	                name: 'priceSupKilo',
		      	              	minValue: 0,
		      	                allowBlank:false,
		      	            	blankText: '<bean:message key="price.priceSupKilo.required" />',
		      	            	hideTrigger: true,
								editable: false,
		      	              	keyNavEnabled: false,
		      	              	mouseWheelEnabled: false,
		      	              	validator: function(value){
		      	              		Ext.getCmp('priceInfKilo').validate();
		      	              		return true;
		      	              	}
		      	            }
	                        ]
	                    },{
	      	        		xtype:'container',html:'<br/>'
	      	        	},{
	                        xtype: 'container',
	                        layout: 'hbox',
	                        defaultType: 'textfield',
	                        defaults:{
	                        	labelAlign:'right',
	                        	margin: globalMargin
	                        },
	                        items: [{
    	      	              	xtype: 'combo',
    	      	                fieldLabel: '<bean:message key="price.oriCtrDesc"/>',
    	      	                id:'oriCtrId',
    	      	                name: 'oriCtrId',
    					        store: Ext.create('Ext.data.Store',{
    	      			        	model: 'modelOriCtrId',
    	      			        	data: {registers: <%=request.getAttribute("oriCountriesCollection")%>},
    	      			        	proxy:{
    	      			        		type: 'memory',
    	      			        		reader: {
    	      			                    type: 'json',
    	      			                    root: 'registers'
    	      			                }
    	      			        	},
    	      			        	sorters: [{
    	      				            property: 'oriCtrDesc',
    	      				            direction: 'ASC'
    	      				        }]
    	      			        }),
    	      	              	lastQuery:'',
    					        queryMode: 'local',
    					        displayField: 'oriCtrDesc',
    					        valueField: 'oriCtrId',
    					        forceSelection: true,
    					        //value: userCtrId,
    					        emptyText: '<bean:message key="combo.emptyText"/>',
    					        listeners:{
    					        	buffer: 50,
    					        	change: function(combo, records, eOpts){
    					        		
    					        		var tmpCmp = Ext.getCmp('oriRegId');
    					        		var strTmpCmp = tmpCmp.store;
    					        		
    					        		if(Ext.isEmpty(combo.getValue())){
	    					        		tmpCmp.disable();
		    					        	tmpCmp.clearValue();
    					        		}else{
	    					        		tmpCmp.enable();
    					        		}
    					        		
    					        		strTmpCmp.suspendEvents();
    					        		strTmpCmp.clearFilter();
    					        		strTmpCmp.resumeEvents();

    					        		strTmpCmp.filter([
    					        			{id: 'oriCtrId', property: 'oriCtrId', value: combo.getValue(), exactMatch: true}
    					        		]);
    					        		
    					        		if(strTmpCmp.getCount() <= 1){
    					        			tmpCmp.disable();
    					        			tmpCmp.clearValue();
    					        		}else{
	    					        		tmpCmp.enable();
    					        		}
    					        	},
    					        	afterrender:function(){
    					        			var tmpStore = this.store;
        					        		
        					        		tmpStore.suspendEvents();
        					        		tmpStore.clearFilter();
        					        		tmpStore.resumeEvents();
        					        		this.setValue(userCtrId);
    					        		
    					        	}
    					        }
    					    },{
    	      	              	xtype: 'combo',
    	      	                fieldLabel: '<bean:message key="price.oriRegDesc"/>',
    	      	                id:'oriRegId',
    	      	                name: 'oriRegId',
    	      	              	allowBlank:true,
    	      	              	disabled: true,
    	      	              	lastQuery:'', //! IMPORTANT <-- if not set to empty, the first filter doesn't work!
    	      	              	store: Ext.create('Ext.data.Store',{
    	      			        	model: 'modelOriRegId',
    	      			        	data: {registers: <%=request.getAttribute("oriRegionsCollection")%>},
    	      			        	proxy:{
    	      			        		type: 'memory',
    	      			        		reader: {
    	      			                    type: 'json',
    	      			                    root: 'registers'
    	      			                }
    	      			        	},
    	      			        	sorters: [{
    	      				            property: 'oriRegDesc',
    	      				            direction: 'ASC'
    	      				        }]
    	      			        }),
    					        queryMode: 'local',
    					        displayField: 'oriRegDesc',
    					        valueField: 'oriRegId',
    					        editable: true,
    					        forceSelection: true,
    					        emptyText: '<bean:message key="combo.emptyText"/>',
    					        listeners:{
    					        	buffer: 50,
    					        	specialkey: function(field, e){
    				                    // e.HOME, e.END, e.PAGE_UP, e.PAGE_DOWN,
    				                    // e.TAB, e.ESC, arrow keys: e.LEFT, e.RIGHT, e.UP, e.DOWN
    				                    if (e.getKey() == e.BACKSPACE) {
    				                        if(Ext.isEmpty(field.getValue())){
    				                    		new Ext.util.DelayedTask().delay(100,function(){
    				                    			Ext.getCmp('oriCtrId').fireEvent('change', Ext.getCmp('oriCtrId'));
    				                    		});
    				                        }
    				                    }
    				                },
    					        	change: function(combo, records, eOpts){
    					        		
    					        		var tmpCmp = Ext.getCmp('oriProvId');
    					        		var strTmpCmp = tmpCmp.store;
    					        		
    					        		if(Ext.isEmpty(combo.getValue())){
	    					        		tmpCmp.disable();
		    					        	tmpCmp.clearValue();
    					        		}else{
	    					        		tmpCmp.enable();
    					        		}
    					        		
    					        		strTmpCmp.suspendEvents();
    					        		strTmpCmp.clearFilter();
    					        		strTmpCmp.resumeEvents();

    					        		strTmpCmp.filter([
    					        			{id: 'oriRegId', property: 'oriRegId', value: combo.getValue(), exactMatch: true}
    					        		]);
    					        		
    					        		if(strTmpCmp.getCount() <= 1){
    					        			tmpCmp.disable();
		    					        	tmpCmp.clearValue();
    					        		}else{
	    					        		tmpCmp.enable();
    					        		}
    					        	}
    					        }
    	      	            }
    					    ]
	                    },{
	                        xtype: 'container',
	                        layout: 'hbox',
	                        defaultType: 'textfield',
	                        defaults:{
	                        	labelAlign:'right',
	                        	margin: globalMargin
	                        },
	                        items: [{
    	      	              	xtype: 'combo',
    	      	                fieldLabel: '<bean:message key="price.oriProvDesc"/>',
    	      	                id:'oriProvId',
    	      	                name: 'oriProvId',
    	      	              	allowBlank:true,
    	      	              	disabled: true,
    	      	              	lastQuery:'', //! IMPORTANT <-- if not set to empty, the first filter doesn't work!
    	      	              	store: Ext.create('Ext.data.Store',{
    	      			        	model: 'modelOriProvId',
    	      			        	data: {registers: <%=request.getAttribute("oriProvincesCollection")%>},
    	      			        	proxy:{
    	      			        		type: 'memory',
    	      			        		reader: {
    	      			                    type: 'json',
    	      			                    root: 'registers'
    	      			                }
    	      			        	},
    	      			        	sorters: [{
    	      				            property: 'oriProvDesc',
    	      				            direction: 'ASC'
    	      				        }]
    	      			        }),
    					        queryMode: 'local',
    					        displayField: 'oriProvDesc',
    					        valueField: 'oriProvId',
    					        editable: true,
    					        forceSelection: true,
    					        emptyText: '<bean:message key="combo.emptyText"/>',
    					        listeners:{
    					        	buffer: 50,
    					        	specialkey: function(field, e){
    				                    // e.HOME, e.END, e.PAGE_UP, e.PAGE_DOWN,
    				                    // e.TAB, e.ESC, arrow keys: e.LEFT, e.RIGHT, e.UP, e.DOWN
    				                    if (e.getKey() == e.BACKSPACE) {
    				                        if(Ext.isEmpty(field.getValue())){
    				                    		new Ext.util.DelayedTask().delay(100,function(){
    				                    			Ext.getCmp('oriRegId').fireEvent('change', Ext.getCmp('oriRegId'));
    				                    		});
    				                        }
    				                    }
    				                }
    					        }
    	      	            }
	                        ]
	                    }
	                    ]//Items Fieldset
	      	        }]//Items Form.Panel
				});
				
								
				//*************************** CUSTOM GRID PANEL ********************************
				
				Ext.define('GridModel', {
			        extend: 'Ext.data.Model',
			        fields: [
						{name: 'priceId', type: 'number', mapping:'priceid'},
			            {name: 'dateFieldFiltro', type: 'date', mapping:'datefield', dateFormat:'d/m/Y'},
			            {name: 'prdVarSk', type: 'number', mapping:'prdvarsk'},
			            {name: 'quaSk', type: 'number', mapping:'quask'},
			            {name: 'sizeSk', type: 'number', mapping:'sizesk'},
			            {name: 'tunitSk', type: 'number', mapping:'tunitsk'},
			            {name: 'oriSk', type: 'number', mapping:'orisk'},
			            {name: 'ctrSkCmb', type: 'number', mapping:'ctrsk'},
			            {name: 'clevSk', type: 'number', mapping:'clevsk'},
			            
			            {name: 'oriCtrId', type: 'number', mapping:'orictrid'},
			            {name: 'oriRegId', type: 'number', mapping:'oriregid'},
			            {name: 'oriProvId', type: 'number', mapping:'oriprovid'},
			            {name: 'prodId', type: 'number', mapping:'prodid'},
			            {name: 'varId', type: 'number', mapping:'varid'},
			            
			            {name: 'priceCopyFlagText', type: 'string', mapping:'pricecopyflagtext'},
						{name: 'priceInfUni', type: 'number', mapping:'priceinfuni'},
			            {name: 'priceSupUni', type: 'number', mapping:'pricesupuni'},
			            {name: 'priceInfKilo', type: 'number', mapping:'priceinfkilo'},
			            {name: 'priceSupKilo', type: 'number', mapping:'pricesupkilo'},
			            
			            {name: 'oriCtrDesc', type: 'string', mapping: ctrMapping},
			            {name: 'oriRegDesc', type: 'string', mapping:'oriregdesc'},
			            {name: 'oriProvDesc', type: 'string', mapping:'oriprovdesc'},
			            {name: 'tunitMeasure', type: 'string', mapping:'tunitmeasure'},
			            {name: 'tunitKilo', type: 'number', mapping:'tunitkilo'},
			            {name: 'sizeDesc', type: 'string', mapping:'sizedesc'},
			            {name: 'quaDesc', type: 'string', mapping:'quadesc'},
			            {name: 'prodDesc', type: 'string', mapping:'proddesc'},
			            {name: 'varDesc', type: 'string', mapping:'vardesc'},
			            {name: 'clevDesc', type: 'string', mapping:'clevdesc'},
						{name: 'ptypeId', type: 'number', mapping:'ptypeid'}
			        ],
			        idProperty: 'priceId'
			    });
				
								
				// create the Data Store
			    var store = Ext.create('Ext.data.Store', {
			        id: 'dataStore',
			        //pageSize: 50,
			        model: 'GridModel',
			        remoteSort: false,
			        remoteFilter: false,
			        remoteGroup:false,
			        // allow the grid to interact with the paging scroller by buffering
			        //buffered: true,
			        proxy: {
			            // load using script tags for cross domain, if the data in on the same domain as
			            // this page, an HttpProxy would be better
			            type: 'ajax',
			            url: 'priceAction.do?accion=load',
			            extraParams: {
			                ctrSk: Ext.getCmp('ctrSk').getValue(),
			                dateField: Ext.getCmp('dateField').getRawValue()
			            },
			            reader: {
			            	type: 'json',
			                root: 'registers',//'topics',
			                totalProperty: 'totalCount'
			            },
			            // sends single sort as multi parameter
			            simpleSortMode: true
			        },
			        sorters: [{
			            property: 'priceId',
			            direction: 'DESC'
			        }]
			    });//Store
			    
			    var filters = {
		            ftype: 'filters',
		            // encode and local configuration options defined previously for easier reuse
		            encode: false, // json encode the filter query
		            local: true,   // defaults to false (remote filtering)
		            filters: [{
		                type: 'date',
		                dataIndex: 'dateFieldFiltro'
		            }, {
		                type: 'string',
		                dataIndex: 'prodDesc'
		            }, {
		                type: 'string',
		                dataIndex: 'varDesc'
		            }, {
		                type: 'string',
		                dataIndex: 'quaDesc'
		            }, {
		                type: 'string',
		                dataIndex: 'sizeDesc'
		            }, {
		                type: 'string',
		                dataIndex: 'oriCtrDesc'
		            }, {
		                type: 'string',
		                dataIndex: 'oriRegDesc'
		            }, {
		                type: 'list',
		                dataIndex: 'priceCopyFlagText',
						options: ['<bean:message key="price.grid.original"/>','<bean:message key="price.grid.new"/>']
		            }, {
		                type: 'numeric',
		                dataIndex: 'priceInfUni'
		            }, {
		                type: 'numeric',
		                dataIndex: 'priceSupUni'
		            }, {
		                type: 'numeric',
		                dataIndex: 'priceInfKilo'
		            }, {
		                type: 'numeric',
		                dataIndex: 'priceSupKilo'
		            }, {
		                type: 'string',
		                dataIndex: 'tunitMeasure'
		            }]
		        };
			    
			    Ext.create('Ext.custom.GridPanel',{
			    	id:'GridPanel',
			    	beforeSelectionchange: function(){
			    		//Clear the filters in child combo Boxes in order to set the values
			    		var tmpStore = Ext.getCmp('prdVarSk').store;
		        		
		        		tmpStore.suspendEvents();
		        		tmpStore.clearFilter();
		        		tmpStore.resumeEvents();
		        		
		        		tmpStore = Ext.getCmp('oriRegId').store;
		        		
		        		tmpStore.suspendEvents();
		        		tmpStore.clearFilter();
		        		tmpStore.resumeEvents();
		        		
		        		tmpStore = Ext.getCmp('oriProvId').store;
		        		
		        		tmpStore.suspendEvents();
		        		tmpStore.clearFilter();
		        		tmpStore.resumeEvents();
		        		
		        		tmpStore = Ext.getCmp('tunitSk').store;
		        		
		        		tmpStore.suspendEvents();
		        		tmpStore.clearFilter();
		        		tmpStore.resumeEvents();
			    	},
			    	width: 900,
			        height: 300,
			        title: '<bean:message key="price.title.table" />',
			        store: store,
			        idForm: 'formulario',
			        features: [filters],
			     	// grid columns
			        columns:[
			        {
			        	xtype: 'rownumberer',
			        	width: 35, 
			        	sortable: false
			        },{
			        	dataIndex: 'priceCopyFlagText',
						text: '',
			        	width: 35, 
			        	sortable: true,
						renderer: function(value, p, record){
							var tmp = record.get('priceCopyFlagText');
							
							if(tmp){
								if(tmp=='<bean:message key="price.grid.original"/>'){
									tmp = '<div style="padding:15px 0 0 20px; background:transparent url(<%=request.getContextPath()%>/images/iconos/exclamation.gif) no-repeat"></div>';
								}else{
									tmp ='';
								}
							}else{
								tmp ='';
							}
							return tmp;
						}
			        },{
			            text: '<bean:message key="price.dateField" />',
			            dataIndex: 'dateFieldFiltro',
			            width: 80,
			            flex:1,
			            renderer : Ext.util.Format.dateRenderer('d/m/Y'),
			            //hidden: true,
			            sortable: true
			        },{
			            text: '<bean:message key="price.prodId" />',
			            dataIndex: 'prodDesc',
			            width: 80,
			            sortable: true
			        },{
			            text: '<bean:message key="price.varId" />',
			            dataIndex: 'varDesc',
			            width: 80,
			            sortable: true
			        },{
			            text: '<bean:message key="price.quaSk" />',
			            dataIndex: 'quaDesc',
			            width: 80,
			            sortable: true
			        },{
			            text: '<bean:message key="price.sizeSk" />',
			            dataIndex: 'sizeDesc',
			            width: 80,
			            sortable: true
			        },{
			            text: '<bean:message key="price.oriCtrDesc" />',
			            dataIndex: 'oriCtrDesc',
			            width: 100,
			            flex:1,
			            //hidden: true,
			            sortable: true
			        },{
			            text: '<bean:message key="price.oriRegDesc" />',
			            dataIndex: 'oriRegDesc',
			            width: 80,
			            sortable: true
			        },{
			            text: '<bean:message key="price.priceInfUni" />',
			            dataIndex: 'priceInfUni',
			            width: 80,
			            renderer : 'usMoney',
			            sortable: true
			        },{
			            text: '<bean:message key="price.priceSupUni" />',
			            dataIndex: 'priceSupUni',
			            width: 80,
			            renderer : 'usMoney',
			            sortable: true
			        },{
			            text: '<bean:message key="price.priceInfKilo" />',
			            dataIndex: 'priceInfKilo',
			            width: 80,
			            renderer : 'usMoney',
			            sortable: true
			        },{
			            text: '<bean:message key="price.priceSupKilo" />',
			            dataIndex: 'priceSupKilo',
			            width: 80,
			            renderer : 'usMoney',
			            sortable: true
			        },{
			            text: '<bean:message key="price.grid.tunitSk" />',
			            dataIndex: 'tunitMeasure',
			            width: 80,
			            sortable: true,
			            renderer: function(value, metaData, record, rowIndex, colIndex, store, view){
			            	var str ='';
			            	str= record.get('tunitMeasure')+' ('+record.get('tunitKilo')+' Kg.)'; 
			            	return str;
			            }
			        }]
			    });

			    // trigger the data store load. We use a delay because the MaskLoad appears in a wrong position. 
			    // So we set a delay to wait for the final component's position inside the container 
			    //new Ext.util.DelayedTask().delay(300,function(){Ext.getStore('dataStore').load();});
			    
	      	});//onReady
		</script>
		
	</tiles:put>
	
	<tiles:put name="title" type="String">
		<bean:message key="price.title" />
	</tiles:put>

	<tiles:put name="encabezado" type="String">
		<bean:message key="price.header" />
	</tiles:put>

	<tiles:put name="contenido" type="String">
		
	</tiles:put>

</tiles:insert>