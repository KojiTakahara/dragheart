package dragheart.service;

import org.springframework.stereotype.Component;

@Component
public class GP2023_1st_SheetPdfService extends PdfService {

	public GP2023_1st_SheetPdfService() {
		super.LEFT = 46;
		super.RIGHT = 320;
		super.START_MAIN = 705;
		super.START_GR = 326;
		super.START_HYPER = 200;
		super.LINE_SPACE = 16.9;
		super.NAME_Y = 91;
		super.NAME_KANA_Y = 124;
		super.ID_X = 40;
		super.DECK_ON_X = 517;
		super.DECK_OFF_X = 539.5;
		super.DECK_Y = 14.5;
		super.FORBIDDENSTAR_ON_X = 245;
		super.FORBIDDENSTAR_OFF_X = 267.5;
		super.FORBIDDENSTAR_Y = 366;
		super.FONT_SIZE = 7;
	}
}