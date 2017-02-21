/*
This is the main widget of the application that carries all UI elements (tabs,buttons,...etc)
*/
define(["dojo/_base/declare",
    "dojo/_base/lang",
    "dojo/text!./templates/MainWidget.html",
    "dijit/layout/TabContainer",
    "dijit/layout/ContentPane",
	"dojo/dom-construct",
	"dojox/grid/DataGrid", 
	"dojo/data/ItemFileWriteStore",
	"dijit/_AttachMixin",
	"dijit/_Widget",
	"dijit/_TemplatedMixin",
	"dijit/_WidgetsInTemplateMixin",
	"icm/base/BasePageWidget",
    "icm/base/_BaseWidget",
	"icm/base/BaseActionContext",
	"ecm/model/Desktop",
	"icm/action/Action",
	"icm/model/Case",
	"dojo/_base/array",
	"ecm/model/Request",
	"dijit/Menu",
    "dijit/MenuItem",
    "dijit/CheckedMenuItem",
    "dijit/MenuSeparator",
    "dijit/PopupMenuItem",
	"icm/widget/menu/MenuManager",
	"icm/widget/menu/ContextualMenu",
	"ecm/widget/dialog/AddContentItemDialog",
	"dojo/dom-style",
	"./ConfirmDialog",
	"dojo/store/Memory",
	"dojo/window",
	"icm/action/workitem/MoveToInbox",
	"icm/model/WorkItem",
	"icm/util/WorkItemHandler",
	"icm/model/properties/controller/ControllerManager",
	"icm/base/Constants"
    ],

    function(declare, lang, 
        template, TabContainer, ContentPane,domConstruct,DataGrid,ItemFileWriteStore,_AttachMixin,_Widget,_TemplatedMixin,_WidgetsInTemplateMixin,BasePageWidget,_BaseWidget,BaseActionContext,Desktop,Action,Case,array,Request
		,Menu,MenuItem,CheckedMenuItem,MenuSeparator,PopupMenuItem,MenuManager,ContextualMenu,AddContentItemDialog,domStyle,ConfirmDialog,Memory,win,MoveToInbox
		,ControllerManager,Constants){
		
		
        return declare("hk.com.sita.widgets.MainWidget.MainWidget",  [_BaseWidget,BasePageWidget, BaseActionContext], {
        	templateString: template,
            widgetsInTemplate: true,
			menu: null,
			myCase: null,
			claimURL: "",
			lapsePeriods: new Object(),
            constructor: function(context){
				this.context=context;
            },
			/*
			This function called automatically after widget creation
			*/
			postCreate: function(){
            	this.inherited(arguments);
				var wid=this;
				var repository=wid.getSolution().getTargetOS();
				
				/*
				Row click event handler for the filings grid
				Enables/Disables button according to the filing type
				Display the filing image in the viewer
				*/
				this.docsDataGrid.on("RowClick", function(evt){
					var idx = evt.rowIndex,
					rowData = wid.docsDataGrid.getItem(idx);
					repository.retrieveItem(rowData.GUID, lang.hitch(wid, function(retItem) {
						var payload = {"contentItem": retItem, "action": "open"};
						wid.onBroadcastEvent("icm.OpenDocument", payload);
					}));
				});
				
				this.btnAddRow.on("Click", function(evt){
					wid.propsDataGrid.store.newItem({ 
						"Allocation_Code" : " ",
						"Account_Code"  : "",
						"Analysis_Code"  : "",
						"Allocation_Amount"  : "",
						"Description_Line"  : "",
						"Report_Amount"  : ""
					});
				});
				
				this.btnDeleteRow.on("Click", function(evt){
					var items = wid.propsDataGrid.selection.getSelected();
					if(items.length){
						dojo.forEach(items, function(selectedItem){
							if(selectedItem !== null){
								var serviceParams = new Object();
								serviceParams.id = selectedItem.Id;
								console.log(selectedItem);
								Request.invokePluginService("sitaPlugin", "DeleteMetaDataByIdService",
								{
									synchronous:true,
									requestParams: serviceParams,
									requestCompleteCallback: function(response) {	// success
										var jsonResponse=dojo.toJson(response, true, "  ");
										var obj = JSON.parse(jsonResponse);
										wid.propsDataGrid.store.deleteItem(selectedItem);
									}
								});
								
							}
						}); 
					}
				});
				
            },

			/*
			This function called automatically passing job information to the widget
			*/
			handleICM_eSoSCaseEvent: function(payload){
				var wid=this;
				var myCase=payload.workItemEditable.getCase();
				this.myCase=myCase;
				var repository=wid.getSolution().getTargetOS();
				
				myCase.retrieveAttributes(lang.hitch(this,function(retAtt){
					var data = {
					  items: []
					};
					
					var serviceParams = new Object();
					serviceParams.Invoice_Number = retAtt.attributes['AP_InvoiceNumber'];
					Request.invokePluginService("sitaPlugin", "GetMetaDataByInvoiceNumberService",
					{
						synchronous:true,
						requestParams: serviceParams,
						requestCompleteCallback: function(response) {	// success
							var jsonResponse=dojo.toJson(response, true, "  ");
							var obj = JSON.parse(jsonResponse);
							array.forEach(obj,function(item){
								data.items.push({ 
									"Id":item.id,
									"Allocation_Code" : item.Allocation_Code,
									"Account_Code"  : item.Account_Code,
									"Analysis_Code"  : item.Analysis_Code,
									"Allocation_Amount"  : item.FC_Allocation_Amount,
									"Description_Line"  : item.Description_Line,
									"Report_Amount"  : item.Report_Amount
								});
							});
						}
					});
					
					/*array.forEach(retItems,function(item){
						serviceInput.push('{"Filing_Number":"'+item.attributes['DocumentTitle']+'","GUID":"'+item.attributes['Id']+'"}');
						var GUID=item.attributes['Id'];
						var filingNumber=item.attributes['DocumentTitle'];
						var filingDate=item.attributes['DateCreated'];
						
						data.items.push({ 
							"GUID":GUID,
							"Filing_Number" : filingNumber,
							"Filing_Date"  : filingDate
						});
						
					});*/
					var store = new ItemFileWriteStore({data: data});
					wid.propsDataGrid.setStore(store);
					var layout = [[
					  {'name': 'id', 'field': 'Id', 'hidden':true},
					  {'name': 'Allocation Code', 'field': 'Allocation_Code', editable: true, 'width': '16%'},
					  {'name': 'Account Code', 'field': 'Account_Code', editable: true, 'width': '16%'},
					  {'name': 'Analysis Code', 'field': 'Analysis_Code', editable: true, 'width': '16%'},
					  {'name': 'FC Allocation Amount', 'field': 'Allocation_Amount', editable: true, 'width': '16%'},
					  {'name': 'Description Line', 'field': 'Description_Line', editable: true, 'width': '16%'},
					  {'name': 'Report Amount', 'field': 'Report_Amount', editable: true, 'width': '16%'}
					]];
					wid.propsDataGrid.setStructure(layout);
					wid.propsDataGrid.resize();
					
					myCase.retrieveCaseFolder(lang.hitch(this,function(retFol){
						wid.setAttributesValues(retAtt);
						retFol.retrieveFolderContents(false, function(results) {
							var items = [];
							var itemIds = [];
							array.forEach(results.items, function(item){
								if(!item.isFolder()){
									items.push(item);
									itemIds.push(item.id.toString().split(',')[2]);
								}
							});
							
							if(itemIds.length){
								var serviceInput=[];
								repository.retrieveMultiItem(itemIds,lang.hitch(this,function(retItems){
									var data = {
									  identifier: "GUID",
									  items: []
									};
									array.forEach(retItems,function(item){
										serviceInput.push('{"Filing_Number":"'+item.attributes['DocumentTitle']+'","GUID":"'+item.attributes['Id']+'"}');
										var GUID=item.attributes['Id'];
										var filingNumber=item.attributes['DocumentTitle'];
										var filingDate=item.attributes['DateCreated'];
										
										data.items.push({ 
											"GUID":GUID,
											"Filing_Number" : filingNumber,
											"Filing_Date"  : filingDate
										});
										
									});
									var store = new ItemFileWriteStore({data: data});
									wid.docsDataGrid.setStore(store);
									var layout = [[
									  {'name': 'id', 'field': 'GUID', 'hidden':true},
									  {'name': 'Document Name', 'field': 'Filing_Number', 'width': '50%'},
									  {'name': 'Creation Date', 'field': 'Filing_Date', 'width': '50%'}
									]];
									wid.docsDataGrid.setStructure(layout);
									wid.docsDataGrid.resize();
									
								}));
							}
						});
					}));
							
						
				}));
				
			},
			/*
			This function called automatically when the save job button clicked
			Saved submitter and sentto information
			*/
			handleICM_SaveCaseEvent: function(payload){
				var wid=this;
				var myCase=payload.workItemEditable.getCase();
				var repository=this.getSolution().getTargetOS();
				myCase.retrieveAttributes(lang.hitch(this,function(retAtt){
					myCase.retrieveCaseFolder(lang.hitch(this,function(retFol){
						var properties = [{"name":"AP_BusinessUnit" ,"value" : wid.txtBusinessUnit.getValue()},
											{"name":"AP_LocationName" ,"value" : wid.txtLocationName.getValue()},
											{"name":"AP_InvoiceNumber" ,"value" : wid.txtInvoiceNumber.getValue()},
											{"name":"AP_InvoiceDate" ,"value" : wid.txtInvoiceDate.getValue()},
											{"name":"AP_SupplierNumber" ,"value" : wid.txtSupplierNumber.getValue()},
											{"name":"AP_SupplierName" ,"value" : wid.txtSupplierName.getValue()},
											{"name":"AP_ProcurementType" ,"value" : wid.txtProcurementType.getValue()},
											{"name":"AP_DocumentAmount" ,"value" : wid.txtDocumentAmount.getValue()},
											{"name":"AP_PONumber" ,"value" : wid.txtPONumber.getValue()},
											{"name":"AP_Currency" ,"value" : wid.txtCurrency.getValue()},
											{"name":"AP_CurrencyRate" ,"value" : wid.txtCurrencyRate.getValue()},
											{"name":"AP_DescriptionHeader" ,"value" : wid.txtDescriptionHeader.getValue()},
											{"name":"AP_Budgeted" ,"value" : wid.txtBudgeted.getValue()},
											{"name":"AP_CloseStatus" ,"value" : wid.txtCloseStatus.getValue()}];
						retFol.saveAttributes(properties,retFol.template,null,null,false,function(response) {
							
						});
					}));
				}));
				for(var i=0;i<wid.propsDataGrid.store._arrayOfTopLevelItems.length;i++){
					if(wid.propsDataGrid.store._arrayOfTopLevelItems[i]==null)
						continue;
					var currItem=wid.propsDataGrid.store._arrayOfTopLevelItems[i];
					var serviceParams = new Object();

					serviceParams.id = currItem.Id;
					serviceParams.Allocation_Code = currItem.Allocation_Code;
					serviceParams.Account_Code = currItem.Account_Code;
					serviceParams.Analysis_Code = currItem.Analysis_Code;
					serviceParams.FC_Allocation_Amount = currItem.Allocation_Amount;
					serviceParams.Description_Line = currItem.Description_Line;
					serviceParams.Report_Amount = currItem.Report_Amount;
					serviceParams.Invoice_Number = wid.txtInvoiceNumber.getValue();
					Request.invokePluginService("sitaPlugin", "InsertMetaDataService",
					{
						synchronous:true,
						requestParams: serviceParams,
						requestCompleteCallback: function(response) {	// success
							var jsonResponse=dojo.toJson(response, true, "  ");
							var obj = JSON.parse(jsonResponse);
						}
					});
				}
				
			},
			setAttributesValues: function(retAtt){
				var wid=this;
				wid.txtBusinessUnit.setValue(retAtt.attributes['AP_BusinessUnit']);
				wid.txtLocationName.setValue(retAtt.attributes['AP_LocationName']);
				wid.txtInvoiceNumber.setValue(retAtt.attributes['AP_InvoiceNumber']);
				wid.txtInvoiceDate.setValue(retAtt.attributes['AP_InvoiceDate']);
				wid.txtSupplierNumber.setValue(retAtt.attributes['AP_SupplierNumber']);
				wid.txtSupplierName.setValue(retAtt.attributes['AP_SupplierName']);
				wid.txtProcurementType.setValue(retAtt.attributes['AP_ProcurementType']);
				wid.txtDocumentAmount.setValue(retAtt.attributes['AP_DocumentAmount']);
				wid.txtPONumber.setValue(retAtt.attributes['AP_PONumber']);
				wid.txtCurrency.setValue(retAtt.attributes['AP_Currency']);
				wid.txtCurrencyRate.setValue(retAtt.attributes['AP_CurrencyRate']);
				wid.txtDescriptionHeader.setValue(retAtt.attributes['AP_DescriptionHeader']);
				wid.txtBudgeted.setValue(retAtt.attributes['AP_Budgeted']);
				wid.txtCloseStatus.setValue(retAtt.attributes['AP_CloseStatus']);
			}
        });
});