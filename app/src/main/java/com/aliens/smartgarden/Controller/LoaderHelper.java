package com.aliens.smartgarden.Controller;

import com.aliens.smartgarden.Model.RecordSituation;
import com.aliens.smartgarden.Parser.DataParser;
import com.aliens.smartgarden.Service.RecordSituationService;

import org.json.JSONException;

/**
 * Created by 12120 on 5/17/2016.
 */
public class LoaderHelper {

    public RecordSituation getLastestSituation(){
        RecordSituationService recordSituationService = new RecordSituationService();
        String data = recordSituationService.getLastestSituation();
        try {
            return DataParser.parseLastestSituation(data);
        } catch (JSONException e) {
            e.printStackTrace();
        }
        return  null;
    }
}
