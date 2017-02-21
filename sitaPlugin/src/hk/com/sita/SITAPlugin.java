/******************************************************************************
 * @Author : RSCA. 
 * @Date 20 Dec 2015
 * @LastModified: Ahmed Alaa@smarTech. 
 * <p>
 * @Description : This CLASS contains all the services in the plugin 
 *
 *******************************************************************************/
package hk.com.sita;

import hk.com.sita.services.DeleteMetaDataByIdService;
import hk.com.sita.services.GetAllMetaDataService;
import hk.com.sita.services.GetMetaDataByIDService;
import hk.com.sita.services.GetMetaDataByInvoiceNumberService;
import hk.com.sita.services.InsertMetaDataService;
import hk.com.sita.services.ReadLookupsService;
import hk.com.sita.services.UpdateMetaDataService;

import java.util.Locale;

import com.ibm.ecm.extension.Plugin;
import com.ibm.ecm.extension.PluginAction;
import com.ibm.ecm.extension.PluginService;

public class SITAPlugin extends Plugin {

	@Override
	public String getId() {
		return "sitaPlugin";
	}

	@Override
	public String getName(Locale locale) {
		return "SITA Plugin";
	}

	@Override
	public String getVersion() {
		return "2.0";
	}

	@Override
	public String getScript() {
		return "sitaPlugin.js";
	}

	@Override
	public String getCSSFileName() {
		return "ICMPluginCSS.css";
	}

	@Override
	public String getConfigurationDijitClass() {
		return "hk.com.sita.widgets.sitaPlugin.ConfigurationPane";
	}

	@Override
	public PluginService[] getServices() {
		return new PluginService[] {

		        new GetAllMetaDataService(),    new GetMetaDataByInvoiceNumberService(),
				new GetMetaDataByIDService(),   new UpdateMetaDataService(),
				new DeleteMetaDataByIdService(),new InsertMetaDataService(),
				new ReadLookupsService()

		};
	}

	

}
