package com.ilimi.taxonomy.controller;

import java.util.Map;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import com.ilimi.common.controller.BaseController;
import com.ilimi.common.dto.Response;
import com.ilimi.common.util.ILogger;
import com.ilimi.common.util.PlatformLogger;
import com.ilimi.taxonomy.mgr.ICompositeSearchManager;

@Controller
@RequestMapping("v2/composite-search")
public class LearningDataSyncController extends BaseController {

	private static ILogger LOGGER = new PlatformLogger(LearningDataSyncController.class.getName());
	
	@Autowired
	private ICompositeSearchManager compositeSearchManager;

	@RequestMapping(value = "/sync/{id:.+}", method = RequestMethod.POST)
	@ResponseBody
	public ResponseEntity<Response> sync(@PathVariable(value = "id") String graphId,
			@RequestParam(name = "objectType", required = false) String objectType,
			@RequestParam(name = "start", required = false) Integer start,
			@RequestParam(name = "total", required = false) Integer total,
			@RequestParam(name = "delete", required = false, defaultValue = "false") boolean delete,
			@RequestBody Map<String, Object> map) {
		String apiId = "ekstep.composite-search.sync";
		LOGGER.log(apiId + " | Graph : " + graphId + " | ObjectType: " + objectType);
		try {
			Response response = compositeSearchManager.sync(graphId, objectType, start, total, delete);
			return getResponseEntity(response, apiId, null);
		} catch (Exception e) {
			LOGGER.log("Error: " , apiId, e);
			return getExceptionResponseEntity(e, apiId, null);
		}
	}
	
	@RequestMapping(value = "/sync/object/{graphId:.+}", method = RequestMethod.POST)
	@ResponseBody
	public ResponseEntity<Response> syncObject(@PathVariable(value = "graphId") String graphId,
			@RequestParam(value = "identifiers", required = true) String[] identifiers,
			@RequestBody Map<String, Object> map) {
		String apiId = "ekstep.composite-search.sync-object";
		LOGGER.log(apiId + " | Graph : " + graphId + " | Identifier: " + identifiers);
		try {
			Response response = compositeSearchManager.syncObject(graphId, identifiers);
			return getResponseEntity(response, apiId, null);
		} catch (Exception e) {
			LOGGER.log("Error: " , apiId, e);
			return getExceptionResponseEntity(e, apiId, null);
		}
	}
}
