
package com.staples.pim.delegate.wercs.corpdmztostep.mapper;

import java.util.HashMap;
import java.util.Map;

import org.springframework.batch.item.file.mapping.FieldSetMapper;
import org.springframework.batch.item.file.transform.FieldSet;
import org.springframework.validation.BindException;

import com.staples.pim.delegate.wercs.common.WercsIntegrationValidation;
import com.staples.pim.delegate.wercs.model.WercsCollectionBean;

public class CorpdmzToStepFieldSetMapper implements FieldSetMapper<WercsCollectionBean> {

	public static final String		headerString				= "A0018#|#A0621#|#A0122#|#A0622#|#A0080#|#A0109#|#A0623#|#A0624#|#A0625#|#A0626#|#A0627#|#A0628#|#A0629#|#A0136#|#A0630#|#A0631#|#A0632#|#A0633#|#A0634#|#A0635#|#A0636#|#A0637#|#A0638#|#A0639#|#A0640#|#A0641#|#A0090#|#A0642#|#A0643#|#A0644#|#A0645#|#A0646#|#A0647#|#A0648#|#A0649#|#A0650#|#A0651#|#A0652#|#A0653#|#A0654#|#A0655#|#A0656#|#A0657#|#A0658#|#A0659#|#A0660#|#A0141#|#A0661#|#A0137#|#A0138#|#A0140#|#A0662#|#A0663#|#A0808#|#A0664#|#A0665#|#A0666#|#A0667#|#A0668#|#A0669#|#A0670#|#A0671#|#A0672#|#A0673#|#A0674#|#A0675#|#A0676#|#A0677#|#A0678#|#A0679#|#A0680#|#A0681#|#A0682#|#A0683#|#A0684#|#A0685#|#A0686#|#A0687#|#A0688#|#A0689#|#A0690#|#A0691#|#A0692#|#A0693#|#A0694#|#A0695#|#A0696#|#A0697#|#A0698#|#A0699#|#A0700#|#A0701#|#A0702#|#A0703#|#A0704#|#A0705#|#A0706#|#A0707#|#A0708#|#A0709#|#A0710#|#A0711#|#A0712#|#A0713#|#A0714#|#A0715#|#A0716#|#A0717#|#A0718#|#A0719#|#A0720#|#A0721#|#A0722#|#A0723#|#A0724#|#A0725#|#A0726#|#A0727#|#A0728#|#A0729#|#A0730#|#A0731#|#A0732#|#A0733#|#A0734#|#A0735#|#A0736#|#A0737#|#A0738#|#A0739#|#A0740#|#A0741#|#A0742#|#A0743#|#A0744#|#A0745#|#A0746#|#A0747#|#A0748#|#A0749#|#A0750#|#A0751#|#A0752#|#A0753#|#A0754#|#A0755#|#A0756#|#A0757#|#A0758#|#A0759#|#A0760#|#A0761#|#A0762#|#A0763#|#A0764#|#A0765#|#A0766#|#A0767#|#A0768#|#A0769#|#A0770#|#A0106#|#A0771#|#A0772#|#A0773#|#A0774#|#A0146#|#A0775#|#A0776#|#A0777#|#A0951#|#A0778#|#A0779#|#A0780#|#A0781#|#A0782#|#A0097#|#A0144#|#A0100#|#A0099#|#A0783#|#A0784#|#A0107#|#A0785#|#A0786#|#A0787#|#A0788#|#A0789#|#A0790#|#A0791#|#A0792#|#A0793#|#A0794#|#A0795#|#A0796#|#A0797#|#A0798#|#A0799#|#A0800#|#A0801#|#A0802#|#A0803#|#A0804#|#A0805#|#A0806#|#A0807#|#A0617#|#A0809#|#A0810#|#A0811#|#A0812#|#A0813#|#A0814#|#A0815#|#A0816#|#A0817#|#A0818#|#A0819#|#A0820#|#A0821#|#A0822#|#A0823#|#A0824#|#A0825#|#A0826#|#A0827#|#A0828#|#A0829#|#A0942#|#A0830#|#A0831#|#A0832#|#A0833#|#A0834#|#A0835#|#A0836#|#A0837#|#A0838#|#A0839#|#A0840#|#A0841#|#A0842#|#A0843#|#A0844#|#A0845#|#A0846#|#A0847#|#A0848#|#A0849#|#A0850#|#A0851#|#A0852#|#A0853#|#A0854#|#A0855#|#A0856#|#A0857#|#A0858#|#A0859#|#A0860#|#A0861#|#A0862#|#A0863#|#A0864#|#A0865#|#A0866#|#A0867#|#A0868#|#A0869#|#A0870#|#A0871#|#A0872#|#A0873#|#A0874#|#A0875#|#A0876#|#A0877#|#A0878#|#A0879#|#A0880#|#A0881#|#A0882#|#A0883#|#A0884#|#A0885#|#A0886#|#A0887#|#A0888#|#A0889#|#A0890#|#A0891#|#A0892#|#A0893#|#A0894#|#A0895#|#A0896#|#A0897#|#A0898#|#A0899#|#A0939#|#A0940#|#A0941#|#A0903#|#A0904#|#A0905#|#A0906#|#A0907#|#A0908#|#A0909#|#A0910#|#A0911#|#A0912#|#A0913#|#A0914#|#A0915#|#A0916#|#A0917#|#A0918#|#A0919#|#A0920#|#A0922#|#A0923#|#A0924#|#A0925#|#A0926#|#A0921#|#A0927#|#A0928#|#A0929#|#A0930#|#A0931#|#A0932#|#A0933#|#A0934#|#A0935#|#A0936#|#A0937#|#A0938#|#A0943";

	public static final String[]	headers						= headerString.split("#//|#", -1);

	public static final String		CONTEXT_ID_VALUE			= "EnglishUS";

	public static final String		EXPORT_CONTEXT_VALUE		= "EnglishUS";

	public static final boolean		USE_CONTEXT_LOCALE_VALUE	= false;

	public static final String		WORKSPACE_ID_VALUE			= "Main";

	@Override
	public WercsCollectionBean mapFieldSet(FieldSet fieldSet) throws BindException {

		WercsCollectionBean wercsCollectionBeanObj = new WercsCollectionBean();
		Map<String, String> inputMap = new HashMap<String, String>();
		String[] names = fieldSet.getNames();

		for (String name : names) {
			inputMap.put(name, fieldSet.readString(name));
		}
		WercsIntegrationValidation validations = new WercsIntegrationValidation();
		Map<String, String> inputMap1 = validations.validationProcess(inputMap);
		wercsCollectionBeanObj.setAttributeValueMap(inputMap1);
		return wercsCollectionBeanObj;
	}

}
