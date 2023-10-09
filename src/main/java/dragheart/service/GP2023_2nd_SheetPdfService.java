package dragheart.service;
import org.springframework.stereotype.Component;

@Component
public class GP2023_2nd_SheetPdfService extends PdfService {

	public GP2023_2nd_SheetPdfService() {
		super.FONT_SIZE = 10;
		super.LEFT = 33;
		super.RIGHT = 418;
		super.START_MAIN = 695;
		super.LINE_SPACE = 28.25;
		super.NAME_Y = 82;
		super.NAME_KANA_Y = 117;
		super.ID_X = 35;
		super.DECK_ON_X = 522;
		super.DECK_OFF_X = 545.5;
		super.DECK_Y = 15.5;
		super.CIRCLE = "";
	}
}