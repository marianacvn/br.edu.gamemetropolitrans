package br.edu.metropolitrans.lwjgl3;

import com.badlogic.gdx.backends.lwjgl3.Lwjgl3Application;
import com.badlogic.gdx.backends.lwjgl3.Lwjgl3ApplicationConfiguration;
import br.edu.metropolitrans.MetropoliTrans;

/** Lança a aplicação desktop (LWJGL3). */
public class Lwjgl3Launcher {
    public static void main(String[] args) {
        if (StartupHelper.startNewJvmIfRequired())
            return; // Isso lida com o suporte ao macOS e ajuda no Windows.
        createApplication();
    }

    private static Lwjgl3Application createApplication() {
        return new Lwjgl3Application(new MetropoliTrans(), getDefaultConfiguration());
    }

    private static Lwjgl3ApplicationConfiguration getDefaultConfiguration() {
        Lwjgl3ApplicationConfiguration configuration = new Lwjgl3ApplicationConfiguration();
        configuration.setTitle("");
        configuration.useVsync(false);
        configuration.setForegroundFPS(Lwjgl3ApplicationConfiguration.getDisplayMode().refreshRate + 1);
        //// Se você remover a linha acima e definir Vsync como falso, você pode obter
        //// FPS ilimitado, o que pode ser
        //// útil para testar o desempenho, mas também pode ser muito estressante para
        //// alguns hardwares.
        //// Você também pode precisar configurar os drivers da GPU para desativar
        //// completamente o Vsync; isso pode causar rasgo de tela.
        configuration.setWindowedMode(1280, 720);
        //// Você pode alterar esses arquivos; eles estão em lwjgl3/src/main/resources/
        //// .
        configuration.setWindowIcon("files/icons/icon-house.png");
        return configuration;
    }
}