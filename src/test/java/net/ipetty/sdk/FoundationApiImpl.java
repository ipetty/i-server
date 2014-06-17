package net.ipetty.sdk;

import java.util.Arrays;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

import net.ipetty.sdk.common.ApiContext;
import net.ipetty.sdk.common.BaseApi;
import net.ipetty.vo.Option;

import org.springframework.util.LinkedMultiValueMap;

/**
 * FoundationApiImpl
 * 
 * @author luocanfeng
 * @date 2014年6月13日
 */
public class FoundationApiImpl extends BaseApi implements FoundationApi {

	public FoundationApiImpl(ApiContext context) {
		super(context);
	}

	private static final String URI_OPTIONS = "/options";

	/**
	 * 获取指定选项组的所有选项
	 */
	@Override
	public List<Option> listOptionsByGroup(String group) {
		LinkedMultiValueMap<String, String> request = new LinkedMultiValueMap<String, String>();
		request.add("group", group);
		return Arrays.asList(context.getRestTemplate().postForObject(buildUri(URI_OPTIONS), request, Option[].class));
	}

	/**
	 * 获取指定选项组的所有选项，并映射成值与文本描述的Map返回
	 */
	@Override
	public Map<String, String> getOptionValueLabelMapByGroup(String group) {
		List<Option> options = this.listOptionsByGroup(group);
		Map<String, String> valueTextMap = new LinkedHashMap<String, String>();
		for (Option option : options) {
			valueTextMap.put(option.getValue(), option.getLabel());
		}
		return valueTextMap;
	}

}
