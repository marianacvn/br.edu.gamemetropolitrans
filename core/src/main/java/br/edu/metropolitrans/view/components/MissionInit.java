package br.edu.metropolitrans.view.components;

import java.util.HashMap;

import br.edu.metropolitrans.MetropoliTrans;
import br.edu.metropolitrans.view.components.mission_modal.MissionComponents;
import br.edu.metropolitrans.view.screens.GameScreen;

public class MissionInit {

	public static void inicializarComponentesMissao(HashMap<String, MissionComponents> missionComponents,
			MetropoliTrans jogo) {
		float baseX = ((GameScreen.TELA_LARGURA - 530) / 2);
		float baseY = (GameScreen.TELA_ALTURA - 400) / 2;

		// region missao 1
		MissionComponents missao0 = new MissionComponents(0, jogo);
		missao0.adicionarTituloMissao("Missão 1: ", baseX + 15, baseY);
		missionComponents.put("missao0", missao0);
		missao0.adicionarOpcaoImagem("mission0-option1", "mission0-option1.png", true, baseX + 5,
				baseY + 230, false);
		missao0.adicionarOpcaoImagem("mission0-option2", "mission0-option2.png", false, baseX + 65,
				baseY + 35, false);
		missao0.adicionarImagemCena("mission0-scene.png", baseX + 250, baseY + 75);
		missao0.adicionarBotaoConfirmar(baseX + 250 + 50, baseY + 35, false);
		missionComponents.put("missao0", missao0);

		MissionComponents missao1 = new MissionComponents(1, jogo);
		missao1.adicionarTituloMissao("Missão 2: ", baseX + 15, baseY);
		missao1.adicionarOpcaoImagem("mission1-option1", "mission1-option1_reduced.png", false, baseX + 15,
				baseY + 265, true);
		missao1.adicionarOpcaoImagem("mission1-option2", "mission1-option2_reduced.png", false, baseX + 15,
				baseY + 265 - 50 - 15, true);
		missao1.adicionarOpcaoImagem("mission1-option3", "mission1-option3_reduced.png", true, baseX + 15,
				baseY + 200 - 50 - 15, true);
		missao1.adicionarOpcaoImagem("mission1-option4", "mission1-option4_reduced.png", false, baseX + 15,
				baseY + 135 - 50 - 15, true);
		missao1.adicionarOpcaoImagem("mission1-option5", "mission1-option5_reduced.png", false, baseX + 15,
				baseY + 70 - 50 - 15, true);
		missao1.adicionarImagemCena("mission1-scene.png", baseX + 250, baseY + 75);
		missao1.adicionarBotaoConfirmar(baseX + 250 + 50, baseY + 35, true);
		missionComponents.put("missao1", missao1);

		// endregion

		// region missao 2

		MissionComponents missao2 = new MissionComponents(2, jogo);
		missao2.adicionarTituloMissao("Missão 3: ", baseX + 15, baseY);
		missao2.adicionarOpcaoImagem("mission2-option1", "mission2-option1.png", false, baseX + 15,
				baseY + 265, true);
		missao2.adicionarOpcaoImagem("mission2-option2", "mission2-option2.png", false, baseX + 15,
				baseY + 265 - 50 - 15, true);
		missao2.adicionarOpcaoImagem("mission2-option3", "mission2-option3.png", false, baseX + 15,
				baseY + 200 - 50 - 15, true);
		missao2.adicionarOpcaoImagem("mission2-option4", "mission2-option4.png", true, baseX + 15,
				baseY + 135 - 50 - 15, true);
		missao2.adicionarOpcaoImagem("mission2-option5", "mission2-option5.png", false, baseX + 15,
				baseY + 70 - 50 - 15, true);
		missao2.adicionarImagemCena("mission2-scene.png", baseX + 250, baseY + 75);
		missao2.adicionarBotaoConfirmar(baseX + 250 + 50, baseY + 35, true);
		missionComponents.put("missao2", missao2);

		// endregion

		// region missao 3

		MissionComponents missao3 = new MissionComponents(3, jogo);
		missao3.adicionarTituloMissao("Missão 4: ", baseX + 15, baseY);
		missao3.adicionarOpcaoImagem("mission3-option1", "mission3-option1.png", false, baseX + 15,
				baseY + 265, true);
		missao3.adicionarOpcaoImagem("mission3-option2", "mission3-option2.png", false, baseX + 15,
				baseY + 265 - 50 - 15, true);
		missao3.adicionarOpcaoImagem("mission3-option3", "mission3-option3.png", false, baseX + 15,
				baseY + 200 - 50 - 15, true);
		missao3.adicionarOpcaoImagem("mission3-option4", "mission3-option4.png", false, baseX + 15,
				baseY + 135 - 50 - 15, true);
		missao3.adicionarOpcaoImagem("mission3-option5", "mission3-option5.png", true, baseX + 15,
				baseY + 70 - 50 - 15, true);
		missao3.adicionarImagemCena("mission3-scene.png", baseX + 250, baseY + 75);
		missao3.adicionarBotaoConfirmar(baseX + 250 + 50, baseY + 35, true);
		missionComponents.put("missao3", missao3);

		// endregion

		// region missao 4

		MissionComponents missao4 = new MissionComponents(4, jogo);
		missao4.adicionarTituloMissao("Missão 5: ", baseX + 15, baseY);
		missao4.adicionarOpcaoImagem("mission4-option1", "mission4-option1.png", true, baseX + 15,
				baseY + 220, true);
		missao4.adicionarOpcaoImagem("mission4-option2", "mission4-option2.png", false, baseX + 15,
				baseY + 220 - 100 - 10, true);
		missao4.adicionarOpcaoImagem("mission4-option3", "mission4-option3.png", false, baseX + 15,
				baseY + 110 - 100 - 10, true);
		missao4.adicionarImagemCena("mission4-scene.png", baseX + 250, baseY + 75);
		missao4.adicionarBotaoConfirmar(baseX + 250 + 50, baseY + 35, true);
		missionComponents.put("missao4", missao4);

		// endregion

		// region missao 5
		MissionComponents missao5 = new MissionComponents(5, jogo);
		missao5.adicionarTituloMissao("Missão 6: ", baseX + 15, baseY);
		missao5.adicionarOpcaoImagem("mission5-option1", "mission5-option1.png", false, baseX + 15,
				baseY + 265, true);
		missao5.adicionarOpcaoImagem("mission5-option2", "mission5-option2.png", false, baseX + 15,
				baseY + 265 - 50 - 15, true);
		missao5.adicionarOpcaoImagem("mission5-option3", "mission5-option3.png", true, baseX + 15,
				baseY + 200 - 50 - 15, true);
		missao5.adicionarOpcaoImagem("mission5-option4", "mission5-option4.png", false, baseX + 15,
				baseY + 135 - 50 - 15, true);
		missao5.adicionarOpcaoImagem("mission5-option5", "mission5-option5.png", false, baseX + 15,
				baseY + 70 - 50 - 15, true);
		missao5.adicionarImagemCena("mission5-scene.png", baseX + 250, baseY + 75);
		missao5.adicionarBotaoConfirmar(baseX + 250 + 50, baseY + 35, true);
		missionComponents.put("missao5", missao5);

		// endregion

		// region missao 6

		MissionComponents missao6 = new MissionComponents(6, jogo);
		missao6.adicionarTituloMissao("Missão 7: ", baseX + 15, baseY);
		missao6.adicionarOpcaoImagem("mission6-option1", "mission6-option1.png", false, baseX + 15,
				baseY + 265, true);
		missao6.adicionarOpcaoImagem("mission6-option2", "mission6-option2.png", false, baseX + 15,
				baseY + 265 - 50 - 15, true);
		missao6.adicionarOpcaoImagem("mission6-option3", "mission6-option3.png", false, baseX + 15,
				baseY + 200 - 50 - 15, true);
		missao6.adicionarOpcaoImagem("mission6-option4", "mission6-option4.png", false, baseX + 15,
				baseY + 135 - 50 - 15, true);
		missao6.adicionarOpcaoImagem("mission6-option5", "mission6-option5.png", true, baseX + 15,
				baseY + 70 - 50 - 15, true);
		missao6.adicionarImagemCena("mission6-scene.png", baseX + 250, baseY + 75);
		missao6.adicionarBotaoConfirmar(baseX + 250 + 50, baseY + 35, true);
		missionComponents.put("missao6", missao6);

		// endregion

		// region missao 7

		MissionComponents missao7 = new MissionComponents(7, jogo);
		missao7.adicionarTituloMissao("Missão 8: ", baseX + 15, baseY);
		missao7.adicionarOpcaoImagem("mission7-option1", "mission7-option1.png", false, baseX + 15,
				baseY + 265, true);
		missao7.adicionarOpcaoImagem("mission7-option2", "mission7-option2.png", false, baseX + 15,
				baseY + 265 - 50 - 15, true);
		missao7.adicionarOpcaoImagem("mission7-option3", "mission7-option3.png", false, baseX + 15,
				baseY + 200 - 50 - 15, true);
		missao7.adicionarOpcaoImagem("mission7-option4", "mission7-option4.png", true, baseX + 15,
				baseY + 135 - 50 - 15, true);
		missao7.adicionarImagemCena("mission7-scene.png", baseX + 250, baseY + 75);
		missao7.adicionarBotaoConfirmar(baseX + 250 + 50, baseY + 35, true);
		missionComponents.put("missao7", missao7);

		// endregion

	}

}
