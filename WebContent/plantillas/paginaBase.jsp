<%@page import="org.apache.commons.lang.StringUtils"%>
<%@ page language="java" pageEncoding="ISO-8859-1"%>
<%@ taglib uri="/WEB-INF/struts-logic.tld" prefix="logic"%>
<%@ taglib uri="/WEB-INF/struts-bean.tld" prefix="bean"%>
<%@ taglib uri="/WEB-INF/struts-html.tld" prefix="html"%>
<%@ taglib uri="/WEB-INF/struts-tiles.tld" prefix="tiles"%>

<%@ page import="org.promefrut.simefrut.struts.administration.forms.UserForm"%>
<%@ page import="java.util.ResourceBundle,org.apache.struts.Globals,java.util.Locale"%>
<%@ page import="org.promefrut.simefrut.utils.LookUpResourceSchema"%>
<%@ page import="java.util.ArrayList, java.util.List, java.util.Map, java.util.Iterator"%>


<%
	ResourceBundle mensajes = ResourceBundle.getBundle(LookUpResourceSchema.APPLICATION_RESOURCE,(Locale) request.getSession().getAttribute(Globals.LOCALE_KEY));
	String contextPath = request.getContextPath();
	
	//Language setting for Ext JS 
	String lenguaje = ((Locale)(request.getSession(true).getAttribute(Globals.LOCALE_KEY))).getCountry();
	String location="";
	
	if("sv".equals(lenguaje.toLowerCase())){
		location = "es";
	}else if("us".equals(lenguaje.toLowerCase())){
		location = "en";
	}
%>
<!DOCTYPE html PUBLIC "-//W3C//DTD XHTML 1.0 Transitional//EN" "http://www.w3.org/TR/xhtml1/DTD/xhtml1-transitional.dtd">
<html>
	<head>
		<%--<meta http-equiv="Content-Type" content="text/html; charset=ISO-8859-1">
		<meta http-equiv="Cache-Control" content="none">
        <meta http-equiv="Pragma" content="no-cache">
        --%>
		<title><tiles:getAsString name="title" /></title>
		
		<link type="image/x-icon" href="<%=contextPath%>/images/favicon2.ico" rel="shortcut icon" />
		<link type="image/x-icon" href="<%=contextPath%>/images/favicon.ico" rel="icon" />
        <link rel="SHORTCUT ICON" href="<%=contextPath%>/images/favicon.png" type="image/x-icon" />
		
		<link href="<%=contextPath%>/Ext JS 4.0.7/resources/css/ext-all-gray-4.1.0-b3.css" rel="stylesheet" type="text/css"/>
		<link href="<%=contextPath%>/Ext JS 4.0.7/resources/css/ux/ItemSelector.css" rel="stylesheet" type="text/css" />
		<link href="<%=contextPath%>/Ext JS 4.0.7/resources/css/ux/statusbar.css"	rel="stylesheet" type="text/css" />
		<link href="<%=contextPath%>/Ext JS 4.0.7/resources/css/ux/grid/GridFilters.css" rel="stylesheet" type="text/css" />
		<link href="<%=contextPath%>/Ext JS 4.0.7/resources/css/ux/grid/RangeMenu.css" rel="stylesheet" type="text/css" />
		
		<tiles:getAsString name="css" />
		
		<style type="text/css">
			p {
			    margin:5px;
			}
			
			.settings {
			    background:url(<%=contextPath%>/images/iconos/folder_wrench.png) no-repeat 0 2px;
			}
			
			.nav {
			    background-image:url(<%=contextPath%>/images/iconos/folder_go.png);
			}
			
			.info {
			    background-image:url(<%=contextPath%>/images/iconos/information.png);
			}
			
			.help {
			    background-image:url(<%=contextPath%>/images/iconos/help.png);
			    background-size: 16px;
			}
			
			/*BaseTop CSS Class !Important */
			#app-header-user {
			    /*color: #596F8F;*/
			    color: #000000;
			    font-size: 14px;
			    font-weight: 20;
			    padding: 1px 0px;
			    text-shadow: 0 1px 0 #fff;
			}
			
			#app-header-banner {
			    /*color: #596F8F;
			    color: #CC0000;
			    font-size: 26px;
			    font-weight: bold;
			    font-family: serif;
			    padding: 0px 15px;
			    text-shadow: 0 1px 0 #fff;*/
			    /*background-image: url(images/simefrut.png) ;*/
			    background-image: url(images/LogoSimefrutB3.jpg);
			    background-size: 100px;
			    background-repeat: no-repeat;
			    background-position:center;
			    width:100%;
			    height: 25px;
			}
			
			/*   MENU   */
			.option-item{
			    margin-top: 3px;
			    padding-left: 20px;
			    font-size: 11px;
			    line-height: 20px;
			    cursor: pointer;
			    border: 1px solid #fff;
			}
			
			.option-item-user-female{
				background: url('<%=contextPath%>/images/iconos/user_female.png') no-repeat 0 2px;
			}
			
			.option-item-application-go{
				background: url('<%=contextPath%>/images/iconos/application_go.png') no-repeat 0 2px;
			}
			
			.option-item-member{
				background: url('<%=contextPath%>/images/iconos/member.png') no-repeat 0 2px;
			}
			
			.option-item-greater-than{
				background: url('<%=contextPath%>/images/iconos/greater_than.png') no-repeat 0 2px;
			}
			
			.option-item-information {
			    background: url('<%=contextPath%>/images/iconos/information.png') no-repeat 0 2px;
			}
			
			.option-item-cog{
				background: url('<%=contextPath%>/images/iconos/cog.png') no-repeat 0 2px;
			}
			
			.option-item-book{
				background: url('<%=contextPath%>/images/iconos/book.png') no-repeat 0 2px;
			}
			
			.option-item-subscriptions{
				background: url('<%=contextPath%>/images/iconos/subscriptions.png') no-repeat 0 2px;
			}
			
			.option-item-bogus{
				background: url('<%=contextPath%>/images/iconos/bogus.png') no-repeat 0 2px;
			}
			
			.option-item-logout{
				background: url('<%=contextPath%>/images/iconos/logout.png') no-repeat 0 2px;
			}
			
			.option-item-money-add{
				background: url('<%=contextPath%>/images/iconos/money_add.png') no-repeat 0 2px;
			}
			
			.option-item-tickets{
				background: url('<%=contextPath%>/images/iconos/tickets.png') no-repeat 0 2px;
			}
			
			.option-item-view{
				background: url('<%=contextPath%>/images/iconos/view.gif') no-repeat 0 2px;
			}
			
			.option-item-grid{
				background: url('<%=contextPath%>/images/iconos/grid.png') no-repeat 0 2px;
			}
			
			.option-item-group16{
				background: url('<%=contextPath%>/images/iconos/group16.gif') no-repeat 0 2px;
			}
			
			.option-item-delete{
				background: url('<%=contextPath%>/images/iconos/delete.png') no-repeat 0 2px;
			} 
			
			.option-item-add{
				background: url('<%=contextPath%>/images/iconos/add.png') no-repeat 0 2px;
			}
			
			.option-item-chart-line{
				background: url('<%=contextPath%>/images/iconos/chart_line.png') no-repeat 0 2px;
			}
			
			.option-item-balance-unbalance{
				background: url('<%=contextPath%>/images/iconos/balance_unbalance.png') no-repeat 0 2px;
			}
			
			.option-item-table-refresh{
				background: url('<%=contextPath%>/images/iconos/table_refresh.png') no-repeat 0 2px;
			}
			
			.option-item-shake-hands{
				background: url('<%=contextPath%>/images/iconos/shake-hands.png') no-repeat 0 2px;
			}
			
			.option-item-variation-rate-chart{
				background: url('<%=contextPath%>/images/iconos/variation_rate_chart.png') no-repeat 0 2px;
			}
			
			.option-item-Groups-Meeting-Light-icon{
				background: url('<%=contextPath%>/images/iconos/Groups-Meeting-Light-icon.png') no-repeat 0 2px;
			}
			
			.option-item-calendar-icon{
				background: url('<%=contextPath%>/images/iconos/calendar-icon.png') no-repeat 0 2px;
			}
			
			.option-item-market-icon{
				background: url('<%=contextPath%>/images/iconos/market_icon.png') no-repeat 0 2px;
			}
			
			.option-item-revealed-advantage{
				background: url('<%=contextPath%>/images/iconos/revealed_advantage.png') no-repeat 0 2px;
			}
			
			.option-item-goal-flag{
				background: url('<%=contextPath%>/images/iconos/goal_flag.png') no-repeat 0 2px;
			}
			
			.option-item-Trophy-Gold-icon{
				background: url('<%=contextPath%>/images/iconos/Trophy-Gold-icon.png') no-repeat 0 2px;
			}
			
			.option-item-money_delete{
				background: url('<%=contextPath%>/images/iconos/money_delete.png') no-repeat 0 2px;
			}
			
			.option-item-tree-icon{
				background: url('<%=contextPath%>/images/iconos/tree-icon.png') no-repeat 0 2px;
			}
			
			.option-item-cost-icon{
				background: url('<%=contextPath%>/images/iconos/cost-icon.png') no-repeat 0 2px;
			}
			
			.option-item-import-export-icon{
				background: url('<%=contextPath%>/images/iconos/import-export-icon.png') no-repeat 0 2px;
			}
			
			.option-item-airplane-icon{
				background: url('<%=contextPath%>/images/iconos/airplane-icon.png') no-repeat 0 2px;
			}
			
			.option-item-farmer-icon{
				background: url('<%=contextPath%>/images/iconos/farmer_icon.png') no-repeat 0 2px;
			}
			
			.option-item-industry{
				background: url('<%=contextPath%>/images/iconos/industry.png') no-repeat 0 2px;
			}
			
			.option-item-measure-icon{
				background: url('<%=contextPath%>/images/iconos/measure-icon.png') no-repeat 0 2px;
			}
			
			.option-item-size-icon{
				background: url('<%=contextPath%>/images/iconos/size-icon.gif') no-repeat 0 2px;
			}
			
			.option-item-report-hard-add{
				background: url('<%=contextPath%>/images/iconos/report_hard_add.png') no-repeat 0 2px;
			}
			
			.option-item-Earth-SH{
				background: url('<%=contextPath%>/images/iconos/Earth_SH.png') no-repeat 0 2px;
			}
			
			.option-item-FruitBasket{
				background: url('<%=contextPath%>/images/iconos/FruitBasket.png') no-repeat 0 2px;
			}
			
			.option-item-fruits_icon{
				background: url('<%=contextPath%>/images/iconos/fruits_icon.png') no-repeat 0 2px;
			}
			
			.option-item-user{
				background: url('<%=contextPath%>/images/iconos/user.png') no-repeat 0 2px;
			}
			
			.option-item-edit16{
				background: url('<%=contextPath%>/images/iconos/edit16.gif') no-repeat 0 2px;
			}
			
			.option-item-album{
				background: url('<%=contextPath%>/images/iconos/album.gif') no-repeat 0 2px;
			}
			
			.option-item-bullet-blue{
				background: url('<%=contextPath%>/images/iconos/bullet_blue.png') no-repeat 0 2px;
			}
			
			.option-item-attach-icon{
				background: url('<%=contextPath%>/images/iconos/attach_icon.gif') no-repeat 0 2px;
			}
			
			.option-item-comtrade-logo{
				background: url('<%=contextPath%>/images/iconos/comtrade_logo.png') no-repeat 0 2px;
			}
			
			.option-item-FAOSTAT{
				background: url('<%=contextPath%>/images/iconos/FAOSTAT.png') no-repeat 0 2px;
			}
			
			.option-item-faostat-logo-large{
				background: url('<%=contextPath%>/images/iconos/faostat_logo_large.png') no-repeat 0 2px;
			}
			
			.option-item-schedule-icon-fs{
				background: url('<%=contextPath%>/images/iconos/schedule-icon-fs.png') no-repeat 0 2px;
			}
			
			.option-item-faostat-product{
				background: url('<%=contextPath%>/images/iconos/faostat_product.png') no-repeat 0 2px;
			}
			
			.option-item-faostat-groups{
				background: url('<%=contextPath%>/images/iconos/faostat_groups.png') no-repeat 0 2px;
			}
			
			.option-item-disk{
				background: url('<%=contextPath%>/images/iconos/disk.png') no-repeat 0 2px;
			}
			
			.option-item-templates{
				background: url('<%=contextPath%>/images/iconos/templates.png') no-repeat 0 2px;
			}
			
			.option-item-hover {
			    background-color: #eee;
			}
			
			.option-item-movie-icon{
				background: url('<%=contextPath%>/images/iconos/movie_icon.jpg') no-repeat 0 2px;
			}
			
		</style>
		
		<script type="text/javascript" language="Javascript" src="<%=contextPath%>/Ext JS 4.0.7/ext-all-4.1.0.js"></script>
		<!-- <script type="text/javascript" language="Javascript" src="<%=contextPath%>/Ext JS 4.0.7/ext-all-debug-w-comments 4.1.0-b3.js"></script> -->
		<!--<script type="text/javascript" language="Javascript" src="<%=contextPath%>/Ext JS 4.0.7/ext-all-debug-w-comments 4.0.7.js"></script>-->
		
		<script type="text/javascript" src="<%=contextPath%>/Ext JS 4.0.7/ux/form/MultiSelect.js"></script>
		<script type="text/javascript" src="<%=contextPath%>/Ext JS 4.0.7/ux/form/ItemSelector.js"></script>
		
		<script type="text/javascript" language="Javascript" src="<%=contextPath%>/Ext JS 4.0.7/ux/StatusBar.js"></script>
		<script type="text/javascript" language="Javascript" src="<%=contextPath%>/Ext JS 4.0.7/ux/ValidationStatus.js"></script>
		<script type="text/javascript" language="Javascript" src="<%=contextPath%>/js/PaginaBase/BaseTop.js"></script>
		<script type="text/javascript" language="Javascript" src="<%=contextPath%>/js/PaginaBase/BaseGroupOption.js"></script>
		<script type="text/javascript" language="Javascript" src="<%=contextPath%>/js/PaginaBase/BaseMenu.js"></script>
		<script type="text/javascript" language="Javascript" src="<%=contextPath%>/js/PaginaBase/BaseContainer.js"></script>
		<script type="text/javascript" language="Javascript" src="<%=contextPath%>/js/PaginaBase/BaseViewer.js"></script>
		
		
		<!-- Filter JS Declaration -->
		<script type="text/javascript" src="<%=contextPath%>/Ext JS 4.0.7/ux/grid/filter/Filter.js"></script>
		<script type="text/javascript" src="<%=contextPath%>/Ext JS 4.0.7/ux/grid/FiltersFeatures.js"></script>
		<script type="text/javascript" src="<%=contextPath%>/Ext JS 4.0.7/ux/grid/menu/ListMenu.js"></script>
		<script type="text/javascript" src="<%=contextPath%>/Ext JS 4.0.7/ux/grid/menu/RangeMenu.js"></script>
		<script type="text/javascript" src="<%=contextPath%>/Ext JS 4.0.7/ux/grid/filter/BooleanFilter.js"></script>
		<script type="text/javascript" src="<%=contextPath%>/Ext JS 4.0.7/ux/grid/filter/DateFilter.js"></script>
		<script type="text/javascript" src="<%=contextPath%>/Ext JS 4.0.7/ux/grid/filter/ListFilters.js"></script>
		<script type="text/javascript" src="<%=contextPath%>/Ext JS 4.0.7/ux/grid/filter/NumericFilter.js"></script>
		<script type="text/javascript" src="<%=contextPath%>/Ext JS 4.0.7/ux/grid/filter/StringFilter.js"></script>
		
		
		<%-- Internacionalizacion de Ext JS  Must be the last one --%>
		<script type="text/javascript" language="Javascript" src="<%=contextPath%>/Ext JS 4.0.7/locale/ext-lang-<%=location%>.js"></script>
		
		<tiles:getAsString name="scripts" />
		

		<script type="text/javascript">
		    Ext.require(['*']);
			var javaLocation = '<%=location%>';
			var m; //Global message object.
			
		    function showGlobalErrors(){
		    	var globalErrors = document.getElementById('globalErrors').innerHTML;
				globalErrors = Ext.String.trim(globalErrors);
				
				if(globalErrors.length > 0){
					Ext.Msg.show({
						title: '<bean:message key="global.error.title" />',
						msg: globalErrors,
						icon: Ext.Msg.ERROR,
						buttons: Ext.Msg.OK,
						animateTarget: 'globalErrors'
					});
				}		    	
		    }
		    
		    function showGlobalWarnings(){
				var globalWarnings = document.getElementById('globalWarnings').innerHTML;
				globalWarnings = Ext.String.trim(globalWarnings);
				
				if(globalWarnings.length > 0){
					Ext.Msg.show({
						title: '<bean:message key="global.warning.title" />',
						msg: globalWarnings,
						icon: Ext.Msg.WARNING,
						buttons: Ext.Msg.OK,
						animateTarget: 'globalWarnings'
					});
				}
		    }
		    
		    function showGlobalMessages(){
		    	var globalMessages = document.getElementById('globalMessages').innerHTML;
				globalMessages = Ext.String.trim(globalMessages);
				
				if(globalMessages.length > 0){
					Ext.Msg.show({
						title: '<bean:message key="global.attention.title" />',
						msg: globalMessages,
						icon: Ext.Msg.INFO,
						buttons: Ext.Msg.OK,
						animateTarget: 'globalMessages'
					});
				}
		    }
		    
		    /**
			* Generic wait window
			* @return Ext.window.MessageBox
			*/
			function wait(){
				//return Ext.MessageBox.wait('<bean:message key="login.wait.message" />','<bean:message key="login.wait.title" />');
				return Ext.MessageBox.wait('<bean:message key="login.wait.message" />');
			}
		    
		    
			/**
	         * @class Custom.FormPanel
	         * @extends Ext.form.Panel
	         * @author HWM
	         *
	         * The basic Form Panel used for pages
	         * 
	         * @constructor
	         * Create a new Basic Form Panel 
	         * 
	         * @param {Object} config The config object
	        **/
	        Ext.define('Ext.custom.FormPanel',{
	        	extend: 'Ext.form.Panel',
	        	alias:'widget.customFormPanel',
	        	//id:'formulario',
	        	standardSubmit: true,
	        	renderTo:'container',
	        	frame:true,
	            bodyStyle:'padding:5px 5px 0 5px',
	            width: 350,
				habBtnDelete: false, //If true shows the Delete Button
	            
	            // CUSTOM PROPERTIES
	            labelWidth: 175,
	            cancelFn: null,
	            insertFn: null,
	            updateFn: null,
				deleteFn: null,
	            afterCancelFn: null,
	            onSuccess: null,
	            onFailure: null,
	            send: function(accion){
	            	var form = this;
					if (form.getForm().isValid()){
		                Ext.getCmp('accion').setValue(accion);
		                var valid = true;
						
						if(accion=='insert' || accion=='ajaxInsert'){
							if(form.insertFn){
								valid = false;
	          	            	valid = form.insertFn();
	          	            }
						}else if(accion=='update' || accion=='ajaxUpdate'){
							if(form.updateFn){
								valid = false;
	          	            	valid = form.updateFn();
	          	            }
						}else if(accion=='delete' || accion=='ajaxDelete'){
							Ext.Msg.show({
								scope: this,
								title: '<bean:message key="global.warning.title" />',
								msg: '<bean:message key="global.warning.delete.msg"/>',
								icon: Ext.Msg.WARNING,
								buttons: Ext.Msg.YESNO,
								modal: true,
								fn: function(botton){
									if(botton =='yes'){
										if(form.deleteFn){
											valid = false;
											valid = form.deleteFn();
										}
										
										if(valid){
											form._doCustomSubmit();
										}
									}
								},
								animateTarget: 'globalWarnings'
							});
							
							return false;
						}else{
							alert('accion no configurada');
							return false;
						}
						
						if(typeof valid !='boolean'){
							valid = true;
						}
	          	        
						if(valid){
							form._doCustomSubmit();
						}
		            }
	            },
				_doCustomSubmit: function(){
					m = wait();
					this.submit({
						scope: this,
						clientValidation: false,
						/*	The callback that will be invoked after a successful response (see top of submit and load for a description of what constitutes a successful response). The function is passed the following parameters:
								form : The Ext.form.Basic that requested the action.
								action : The Action object which performed the operation.
								The action object contains these properties of interest:
									response
									result : interrogate for custom postprocessing
									type
						*/
						success: function(form, action){
							m.close();
							var form = this;//Ext.getCmp('formulario');
							if(form.onSuccess){
								form.onSuccess();
							}
							Ext.get('globalMessages').update(action.result.msg);
							showGlobalMessages();
							Ext.getCmp(this.id+'-btnCancel').fireEvent('click');
						},
						
						/**
						*	The callback that will be invoked after a failed transaction attempt. The function is passed the following parameters:
								form : The Ext.form.Basic that requested the action.
								action : The Action object which performed the operation.
								The action object contains these properties of interest:
									failureType: {Ext.form.action.Action} CLIENT_INVALID, CONNECT_FAILURE, LOAD_FAILURE, SERVER_INVALID 
									response
									result : interrogate for custom postprocessing
									type
						*/
						failure: function(form, action){
							m.close();
							var form = this;// Ext.getCmp('formulario');
							if(form.onFailure){
								form.onFailure();
							}
							Ext.get('globalErrors').update(action.result.msg);
							showGlobalErrors();
						}
					});
				},
	            // END CUSTOM PROPERTIES
				
	            fieldDefaults: { 
	                msgTarget: 'side',
	                fieldAlign:'right'
	            },
	            defaults: {
	                anchor: '100%',
	                enforceMaxLength: true
	            },
	            
	            initComponent: function(){
	            	if(Ext.isEmpty(this.url)){
	            		Ext.Error.raise({msg:'The url property is empty "'+this.url+'"'});
	            	}
	            	
	            	if(Ext.isEmpty(this.title)){
	            		Ext.Error.raise({msg:'The title property is empty "'+this.title+'"'});
	            	}
	            	
	            	if(Ext.isEmpty(this.id)){
	            		Ext.Error.raise({msg:'The id property is empty "'+this.id+'"'});
	            	}
	            	
	            	Ext.apply(this, {
	            		fieldDefaults: {
	          	            msgTarget: 'side',
	          	            labelWidth: this.labelWidth
	          	        },
	          	    	dockedItems:[
	        			    Ext.create('Ext.ux.StatusBar', {
	        	                dock: 'bottom',
	        	                id: this.id+'-form-statusbar',
	        	                defaultText: '<bean:message key="statusbar.defaultText" />',
	        	                plugins: Ext.create('Ext.ux.statusbar.ValidationStatus', {id:this.id+'-validationStatus',form: this.id})/*,
	        	                items: [{
	        	      	            text: 'Save',
	        	      	          	handler: function(){
	        							var form = Ext.getCmp('formulario').getForm();
	        							if (form.isValid()) {
	        				                Ext.getCmp('accion').setValue('update');
	        				                
	        				                wait();
	        				                form.submit();
	        				            }
	        						}
	        	      	        	},{
	        	      	            text: 'Cancel',
	        	      	            handler: function(){
	        	      	            	Ext.getCmp('formulario').getForm().reset();
	        	      	            	Ext.getCmp('validationStatus').hideErrors();
	        	      	            	Ext.getCmp('form-statusbar').clearStatus();
	        	      	            }
	        	      	        }]*/
	        	            })
	        			],//DockedItems
	        			buttons: [{
	          	            text: '<bean:message key="opc.insert" />',
	          	            id: this.id+'-btnInsert',
	          	          	listeners: {
								click: function(){
									if(this.up('form.panel').standardSubmit){
										this.up('form.panel').send('insert');
									}else{
										this.up('form.panel').send('ajaxInsert');
									}
								}
							}
	          	        },{
	          	            text: '<bean:message key="opc.update" />',
	          	            id: this.id+'-btnUpdate',
	          	            listeners:{
	          	            	click: function(btn, e){
	          	            		if(this.up('form.panel').standardSubmit){
			          	          		this.up('form.panel').send('update');
		          	          		}else{
		          	          			this.up('form.panel').send('ajaxUpdate');
		          	          		}
	        					},
	        					beforerender: function(btn, eOps){
	        						btn.hide();
	        					}
	          	            }
	          	        },{
	          	            text: '<bean:message key="opc.delete" />',
	          	            id: this.id+'-btnDelete',
	          	            listeners:{
	          	            	click: function(btn, e){
	          	            		if(this.up('form.panel').standardSubmit){
			          	          		this.up('form.panel').send('delete');
		          	          		}else{
		          	          			this.up('form.panel').send('ajaxDelete');
		          	          		}
	        					},
	        					beforerender: function(btn, eOps){
	        						btn.hide();
	        					}
	          	            }
	          	        },{
	          	            text: '<bean:message key="opc.cancel" />',
	          	            id: this.id+'-btnCancel',
	          	            listeners:{
	          	            	click: function(){
	          	            		var valid=true;
	          	            	
	          	            		if(this.up('form.panel').cancelFn){
	          	            			valid = false;
	          	            			valid = this.up('form.panel').cancelFn();
	          	            		}
	          	            	
	          	            		if(typeof valid !='boolean'){
	          	            			valid = true;
	          	            		}
	          	            	
	          	            		if(valid){
		          	            		//Ext.getCmp(idForm).getForm().reset();
		          	            		this.up('form.panel').getForm().reset();
		          	            		Ext.getCmp(this.up('form.panel').id+'-validationStatus').hideErrors();
		          	            		Ext.getCmp(this.up('form.panel').id+'-form-statusbar').clearStatus();
		          	            		this.up('form.panel').insertMode();
		          	            	
		          	            		if(this.up('form.panel').afterCancelFn){
		          	            			this.up('form.panel').afterCancelFn();
		          	            		}
	          	            		}
	          	            	}
	          	            }
	          	        }]//Buttons
	            	});//Apply
	            	
	            	this.callParent(arguments);
	            },
	            
	            updateMode: function(){
	            	Ext.getCmp(this.id + '-btnUpdate').show();
					if(this.habBtnDelete){
						Ext.getCmp(this.id+'-btnDelete').show();
					}
	            	Ext.getCmp(this.id+'-btnInsert').hide();
	            },
	            
	            insertMode: function(){
	            	Ext.getCmp(this.id+'-btnInsert').show();
	            	Ext.getCmp(this.id+'-btnUpdate').hide();
					if(this.habBtnDelete){
						Ext.getCmp(this.id+'-btnDelete').hide();
					}
	            },
				
				reportMode: function(){
					Ext.getCmp(this.id+'-btnInsert').hide();
	            	Ext.getCmp(this.id+'-btnUpdate').hide();
					Ext.getCmp(this.id+'-btnDelete').hide();
					Ext.getCmp(this.id+'-btnCancel').hide();
				},
	            
	            listeners:{
	            	render: function(){
	            		this.add([{
	                		xtype:'hidden',
	                		id:'accion',
	                		name:'accion'
	                	}]);
	            	},
	            	position: function(){
	            		this.setPosition(Ext.get('container').getWidth()/2 - this.width/2, 10);
	            	}
	            }
	        });
	        //*****/
		    
	        
	        /**
	         * @class Custom.GridPanel
	         * @extends Ext.grid.Panel
	         * @author HWM
	         *
	         * The basic Grid Panel used for pages
	         * 
	         * @constructor
	         * Create a new Basic Grid Panel 
	         * 
	         * @param {Object} config The config object
	        **/
	        Ext.define('Ext.custom.GridPanel',{
				extend: 'Ext.grid.Panel',
				alias: 'widget.customGridPanel',
				idAlignTo: '',
				idForm: '',
				beforeSelectionchange: function(){return true;},
				renderTo: 'container',
				afterLoadReg: null,
				
				verticalScrollerType: 'paginggridscroller',
		        loadMask: true,
		        
		        enableColumnHide: false,
		        //disableSelection: true,
		        invalidateScrollerOnRefresh: false,
		        /*viewConfig: {
		            trackOver: false
		        },*/
		        
		        initComponent: function(){
		        	if(Ext.isEmpty(this.store)){
		        		Ext.Error.raise({msg:'The store property is empty "'+this.store+'"'});
		        	}
		        	
		        	if(Ext.isEmpty(this.id)){
		        		Ext.Error.raise({msg:'The id property is empty "'+this.id+'"'});
		        	}
		        	
		        	if(Ext.isEmpty(this.columns)){
		        		Ext.Error.raise({msg:'The columns property is empty'});
		        	}
		        	
		        	if(Ext.isEmpty(this.idForm)){
		        		Ext.Error.raise({msg:'The idForm property is empty"'+this.idForm+'"'});
		        	}
		        	
		        	if(Ext.isEmpty(this.idAlignTo)){
		        		this.idAlignTo = this.idForm;
		        		//Ext.Error.raise({msg:'The idAlignTo property is empty"'+this.idAlignTo+'"'});
		        	}

		        	this.callParent(arguments);
		        },
		        bbar: Ext.create('Ext.toolbar.Toolbar', {
		            items:[
		                   '->',
		                   {
		                       text: '<bean:message key="toolbar.clearFilter"/>',
		                       handler: function () {
		                    	   this.up('grid.panel').filters.clearFilters();
		                       } 
		                   }
		            ]
		        }),/**/
		        listeners:{
		        	position: function(){
	                	if(!Ext.isEmpty(this.idAlignTo)){
			        		this.alignTo(Ext.getCmp(this.idAlignTo),'t-b',[0,10]);
	                	}
	            	},
	            	selectionchange: function(model, records) {
	            		if(!Ext.isEmpty(this.idForm)){
		            		if (records[0]) {
		            			var cont = true;
		            			if(this.beforeSelectionchange){
		            				cont = this.beforeSelectionchange();
		            				
		            				if(cont!=true){
		            					if(Ext.isEmpty(cont)){
		            						cont = true;
		            					}else{
			            					cont = false;
		            					}
		            				}
		            			}
		            			
		            			if(cont){
			                    	var $form = Ext.getCmp(this.idForm);
			                    	$form.getForm().loadRecord(records[0]);
			                    	$form.updateMode();
			                    	
			                    	if(this.afterLoadReg){
			                    		this.afterLoadReg();
			                    	}
		            			}
		                    }
	            		}
	                }
	            }
			});/**/
	        
		    Ext.onReady(function() {
		        Ext.state.Manager.setProvider(Ext.create('Ext.state.CookieProvider',{
		            expires: new Date(new Date().getTime()+(1000*60*60*24*30)), //30 days
		            domain: '<%=request.getServerName()+":"+request.getServerPort()%>/'
		            }
		        ));
				
		    	//Ext.getBody().setStyle({background: 'RGB(255,242,195)'});
		    	Ext.getBody().setStyle({background: 'RGB(255,255,215)'});
		    	
		    	showGlobalErrors();
				
		    	showGlobalWarnings();
		    	
		    	showGlobalMessages();
		    	
		        Ext.QuickTips.init();
		        
		        
		        
		        var app;
		        try{// This is because the CenterNow() function reference cannot be reassigned to the object app. 
					centerNow();
			        app = new BaseViewer.App({
	    	        	id:'appPrincipal',
			        	contentElTop: 'north',
	    	        	menuTitle: '<bean:message key="menus.title" />',
	    	        	onMenuCollapse: centerNow,
	    	        	onMenuExpand: centerNow
	    	        });
		        }catch(e){
		        	app = new BaseViewer.App({
	    	        	id:'appPrincipal',
			        	contentElTop: 'north',
	    	        	menuTitle: '<bean:message key="menus.title" />'
	    	        });
		        }

				
		        //Creacion de grupos y opciones de menu
		        <%

		        List<Map<String,Object>> menu = (List<Map<String,Object>>)session.getAttribute("menuApp");
		        for(Iterator itr = menu.iterator(); itr.hasNext(); ) {
					Map<String,Object> grupoOption = (Map<String,Object>)itr.next();
					
					String prefixGrp = "grp0";
					Integer codeGroup = (Integer)grupoOption.get("codeGroup");
					String nameGroup = (String)grupoOption.get("nameGroup");
					String iconCSSGroup = StringUtils.defaultIfEmpty((String)grupoOption.get("iconCSSGroup"),"");
					String msgPropertyGroup = StringUtils.defaultIfEmpty((String)grupoOption.get("msgPropertyGroup"),"");
					
					if(!"".equals(msgPropertyGroup)){
						try{
							nameGroup = StringUtils.defaultIfEmpty(mensajes.getString(msgPropertyGroup), nameGroup);
						}catch(NullPointerException e){
							;
						}catch(java.util.MissingResourceException mrException){
							;
						}
					}
					
					%>
					
					//JavaScript - Group creation
					app.baseMenu.addGroupMenu({
			        	groupId:'<%=prefixGrp%><%=codeGroup%>',
			        	title: '<%=nameGroup%>',
			        	iconCls: '<%=iconCSSGroup%>',
			        	optCls:'',
			        	optItemSelector: 'div',
			        	optOverItemCls: 'option-item-hover',
			        	optTpl: '<tpl for="."><div class="{css}">{title}</div></tpl>'
			        });
					
					<%
					List<String> optionList = (List<String>)grupoOption.get("opciones");
					
					for(Iterator itr2 = optionList.iterator(); itr2.hasNext(); ) {
						Map<String,String> option = (Map<String,String>)itr2.next();
						
						String visible = String.valueOf(option.get("visible"));
						
						if(visible != null && visible.equals("Y")) {
							String optName = (String)option.get("name");
							String optUrl = (String)option.get("link");
							String optTooltip = (String)option.get("tooltip");
							String optIconCSS = StringUtils.defaultIfEmpty((String)option.get("iconCSS"),"");
							String optMsgProperty = StringUtils.defaultIfEmpty((String)option.get("msgProperty"),"");
							
							if(!"".equals(optMsgProperty)){
								try{
									optName = StringUtils.defaultIfEmpty(mensajes.getString(optMsgProperty), optName);
								}catch(NullPointerException e){
									;
								}catch(java.util.MissingResourceException mrException){
									;
								}
							}
							
							%>
							
							//JavaScript - Option Creation
							app.baseMenu.addMenu({
					        	groupId:'<%=prefixGrp%><%=codeGroup%>',
					        	title:'<%=optName%>',
					        	url: '<%=optUrl%>',
					        	css: 'option-item <%=optIconCSS%>'
					        });
							
							<%
						}
					}
		        }
		        
		        %>
		        
		        
		        
		        <%
					String icon = (String)request.getParameter("icon");
					if(StringUtils.isBlank(icon)){
						icon= "option-item-grid";
					}
				%>
		        app.baseContainer.addTab('idTab1','container',Ext.String.trim(Ext.query('div .titulo')[0].innerHTML), '<%=icon%>');
				
				
				if(!Ext.isEmpty(Ext.String.trim(Ext.query('#help')[0].innerHTML))){
					app.baseContainer.addTab('idTab2','help', '<bean:message key="tab.help" />', 'help');
					
					var tmpContent = Ext.String.trim(Ext.query('#help')[0].innerHTML);
					Ext.query('#help')[0].innerHTML ='';
					/*
					Ext.create('Ext.Panel',{
						//xtype: 'portalpanel',
						id:'pnlHelp',
						renderTo:'help',
						height: 500,
						width: 800,
						//autoHeight:true,
						//autoWidth:true,
						tittle:'Help',
						border: false,
						layout:'fit',
						defaults:{
							margin:'10 10 10 10'
						},
						items: [{
							title: 'Portlet 2',
							html: '<div class="portlet-content">' + tmpContent + '</div>'
							,margin:'10 10 10 10'
						}],
						listeners:{
							position: function(){
								this.setPosition(Ext.get('container').getWidth()/2 - this.width/2, 10);
							}
						}
					});
					
					Ext.getCmp('pnlHelp').fireEvent('position',{"this":Ext.getCmp('pnlHelp')});
					*/
					
					
					Ext.create('widget.panel', {
						id: 'pnlHelp',
						
						renderTo: 'help',
						
						title: '-',
						width: 500,
						height: 600,
						//autoHeight:true,
						frame: false,
						
						html: '<div class="portlet-content" style="margin:10px;">' + tmpContent + '</div>',
						listeners:{
							position: function(){
								this.setPosition(Ext.get('container').getWidth()/2 - this.width/2, 10);
							}
						}
					});
					
					Ext.getCmp('pnlHelp').fireEvent('position',{"this":Ext.getCmp('pnlHelp')});
				}
				
		        //app.restoreState();
		        
		        // get a reference to the HTML element with id "hideit" and add a click listener to it
		        /*Ext.get("hideit").on('click', function(){
		            // get a reference to the Panel that was created with id = 'west-panel'
		            var w = Ext.getCmp('west-panel');
		            // expand or collapse that Panel based on its collapsed property state
		            w.collapsed ? w.expand() : w.collapse();
		        });*/
		    });
		    </script>
		</head>
		<body>
			<div style="display:none">
				<span id="globalErrors" style="display:none">
					<html:messages id="message" property="globalErrors">
			          <bean:write name="message" filter="false" />
			        </html:messages>
			        
					<%--<html:errors property="globalErrors"/>--%>
				</span>
				
				<span id="globalWarnings">
					<html:messages id="message" property="globalWarnings">
			          <bean:write name="message" filter="false" />
			        </html:messages>
				</span>
				
				<span id="globalMessages">
					<html:messages id="message" property="globalMessages">
			          <bean:write name="message" filter="false" />
			        </html:messages>
				</span>
				
			</div>
		
		    <!-- use class="x" to prevent a brief flicker of the content -->
		    <div id="north" class="x-hide-display" style="vertical-align: bottom; font-color:black">
		    	<logic:present name="user">
		    		<div id="app-header-banner" style="text-align:right; font-size: 14px; font-family: Verdana, sans-serif; font-style: italic">
		    			<!--  &nbsp;&nbsp;&nbsp;SIMEFRUT style="textLaign:left; float:left;"-->
		    		<!-- </div>
		    		<div id="app-header-user" style="text-align:right; float: right;font-size: 14px; font-family: Verdana, sans-serif"> -->
                    <%
                    	UserForm user = new UserForm();
                      	String dato = "";
                        user = (UserForm) session.getAttribute("user");
                    %>
                    
                    <%=user.getUsername()%> - <%=user.getNamePerson()%>
                    &nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;                    
                    <b>[</b>
                    <a href="<%=contextPath%>/login.do?accion=logout"> <bean:message key="submenus.cerrarsesion" /></a>
					<b>]</b>
					</div>
	           	</logic:present> 
		    </div>
		    <!-- <div id="Tab1" class="x-hide-display">
		        Tab 1
		    </div>
		     -->
						
			<div id="container" style="overflow:auto; height:100%">
				
					<%-- class="x-hide-display" 
					<img src="<%=contextPath%>/images/logoPROMEFRUT.jpg" height="100%" style="position: absolute; top:0%; left:15%; opacity: 0.20; filter:alpha(opacity=20); "/>
					<iframe src="" width="100%" height="100%"></iframe>
					<hr width="97%" style="margin-left:-2px;margin-top:-1px;"/>
					 --%>
					<div class="titulo" style="display:none">
						<tiles:insert name="encabezado" />
					</div>
					<tiles:insert name="contenido" />
				
			</div>
			
			<div id="help" style="overflow:auto; height:100%; margin: 10px;">
				<tiles:insert name="help" />
			</div>
	</body>
</html>
