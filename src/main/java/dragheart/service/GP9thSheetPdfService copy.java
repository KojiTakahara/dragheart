package dragheart.service;
import org.springframework.stereotype.Component;

@Component
public class GP9thSheetPdfService extends PdfService {

	public GP9thSheetPdfService() {
		super.LEFT = 47;
		super.START_MAIN = 711;
    	super.START_GR = 324;
		super.START_HYPER = 194;
		super.LINE_SPACE = 17.2;
		super.NAME_Y = 82;
		super.NAME_KANA_Y = 117;
		super.ID_X = 35;
		super.DECK_ON_X = 522;
		super.DECK_OFF_X = 545.5;
		super.DECK_Y = 15.5;
		super.FORBIDDENSTAR_ON_X = 244;
    	super.FORBIDDENSTAR_OFF_X = 267.5;
		super.FORBIDDENSTAR_Y = 365;
	}
}