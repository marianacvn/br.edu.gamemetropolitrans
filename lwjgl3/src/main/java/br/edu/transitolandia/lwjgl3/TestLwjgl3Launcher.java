package br.edu.transitolandia.lwjgl3;

import com.badlogic.gdx.backends.lwjgl3.Lwjgl3Application;
import com.badlogic.gdx.backends.lwjgl3.Lwjgl3ApplicationConfiguration;
import br.edu.transitolandia.test.StarfishCollector;

/** Lança a aplicação desktop (LWJGL3). */
public class TestLwjgl3Launcher {
    public static void main(String[] args) {
        if (StartupHelper.startNewJvmIfRequired())
            return; // Isso lida com o suporte ao macOS e ajuda no Windows.
        createApplication();
    }

    private static Lwjgl3Application createApplication() {
        return new Lwjgl3Application(new StarfishCollector(), getDefaultConfiguration());
    }

    private static Lwjgl3ApplicationConfiguration getDefaultConfiguration() {
        Lwjgl3ApplicationConfiguration configuration = new Lwjgl3ApplicationConfiguration();
        configuration.setTitle("Testes");
        //// Vsync limita os quadros por segundo ao que seu hardware pode exibir e ajuda
        //// a eliminar
        //// o rasgo de tela. Esta configuração nem sempre funciona no Linux, então a
        //// linha seguinte é uma salvaguarda.
        configuration.useVsync(true);
        //// Limita o FPS à taxa de atualização do monitor atualmente ativo, mais 1 para
        //// tentar corresponder a taxas de atualização fracionárias.
        //// A configuração de Vsync acima deve limitar o FPS real para corresponder ao
        //// monitor.
        configuration.setForegroundFPS(Lwjgl3ApplicationConfiguration.getDisplayMode().refreshRate + 1);
        //// Se você remover a linha acima e definir Vsync como falso, você pode obter
        //// FPS ilimitado, o que pode ser
        //// útil para testar o desempenho, mas também pode ser muito estressante para
        //// alguns hardwares.
        //// Você também pode precisar configurar os drivers da GPU para desativar
        //// completamente o Vsync; isso pode causar rasgo de tela.
        configuration.setWindowedMode(800, 600);
        //// Você pode alterar esses arquivos; eles estão em lwjgl3/src/main/resources/
        //// .
        configuration.setWindowIcon("libgdx128.png", "libgdx64.png", "libgdx32.png", "libgdx16.png");
        return configuration;
    }
}