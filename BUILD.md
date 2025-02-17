Ótimo! Como você está usando **LibGDX 3** com `LWJGL3`, o processo é um pouco diferente, pois o submódulo desktop agora é `lwjgl3`. Vou te guiar na geração do `.jar` corretamente.  

---

### **📌 Passo 1: Verifique a estrutura do projeto**
Seu projeto deve ter a seguinte organização:
```
my-game/
├── core/        # Código principal do jogo
├── lwjgl3/      # Módulo Desktop com LWJGL3
├── android/     # Opcional
├── ios/         # Opcional
├── build.gradle
├── settings.gradle
```

Se o módulo `lwjgl3` não existir, significa que está rodando uma versão antiga do LibGDX (onde era `desktop` ao invés de `lwjgl3`).

---

### **📌 Passo 2: Gere o `.jar` executável**  
Abra um terminal na raiz do projeto e execute:

```sh
./gradlew lwjgl3:jar
```

Isso gerará o `.jar` dentro da pasta:  
```
lwjgl3/build/libs/
```
O nome será algo como:
```
my-game-lwjgl3-1.0.jar
```

---

### **📌 Passo 3: Rodar o Executável**  
Para testar o `.jar` gerado, use:

```sh
java -jar lwjgl3/build/libs/my-game-lwjgl3-1.0.jar
```

Provavelmente, o erro `"No main manifest attribute"` pode ocorrer porque esse `.jar` não inclui todas as dependências necessárias.

---

### **📌 Passo 4: Criar um `.jar` completo (Fat Jar)**
Se quiser um `.jar` que inclua todas as bibliotecas necessárias (**Fat Jar**), altere o `build.gradle` do módulo **lwjgl3** e adicione:

```gradle
jar {
    archiveBaseName.set("my-game")
    archiveVersion.set("")
    
    from {
        configurations.runtimeClasspath.collect { it.isDirectory() ? it : zipTree(it) }
    }
    manifest {
        attributes 'Main-Class': 'com.meu.jogo.Lwjgl3Launcher'
    }
}
```

Depois, gere o `.jar` novamente:

```sh
./gradlew lwjgl3:jar
```

Agora o arquivo estará completo e pode ser executado diretamente com:

```sh
java -jar lwjgl3/build/libs/my-game.jar
```

---

### **📌 Passo 5: Criar um `.exe` (Opcional)**
Se quiser distribuir o jogo como `.exe`, o **Packr** é uma opção:

```sh
./gradlew lwjgl3:pack
```

Isso gera um executável na pasta:
```
lwjgl3/build/packr/
```

---

Isso deve resolver! 🚀 Se der algum erro ou precisar de ajustes, me avise.