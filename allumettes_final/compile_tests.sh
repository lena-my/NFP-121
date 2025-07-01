#!/bin/bash

# --- Configuração Dinâmica ---
# Obtém o caminho absoluto do diretório onde o script está localizado.
# Isso garante que o script funcione independentemente de onde é chamado.
# readlink -f "$0" encontra o caminho real e absoluto do script ($0).
# dirname remove o nome do arquivo, deixando apenas o diretório.
SCRIPT_DIR=$(dirname "$(readlink -f "$0")")

# Define os caminhos com base na localização do script
SRC_DIR="$SCRIPT_DIR/allumettes"
JUNIT_JAR=$(find "$SCRIPT_DIR" -maxdepth 1 -name "junit-platform-console-standalone-*.jar" | head -n 1)

# --- Lógica do Script ---

# Função para exibir mensagens de erro e sair
exit_with_error() {
    echo "❌ Erro: $1"
    exit 1
}

# 1. Pergunta ao usuário se deseja compilar os testes
# O script agora muda para seu próprio diretório para garantir que os caminhos relativos funcionem
# mas as variáveis acima já usam caminhos absolutos, tornando isso mais robusto.
echo "O projeto está localizado em: $SCRIPT_DIR"
read -p "Deseja compilar os testes unitários? (s/n): " compile_tests

# 2. Executa a compilação com base na resposta
if [[ "$compile_tests" == "s" || "$compile_tests" == "S" ]]; then
    # --- COMPILAR COM TESTES ---
    echo "Opção escolhida: Compilar com testes."

    # Verifica se o JAR do JUnit existe no caminho absoluto
    if [ ! -f "$JUNIT_JAR" ]; then
        exit_with_error "O arquivo '$JUNIT_JAR' não foi encontrado."
    fi

    echo "Compilando todos os arquivos (incluindo testes)..."
    # Usa caminhos absolutos para compilar
    javac -g -cp "$JUNIT_JAR:$SRC_DIR" "$SRC_DIR"/*.java

else
    # --- COMPILAR SEM TESTES ---
    echo "Opção escolhida: Compilar sem testes."
    echo "Compilando apenas o código de produção..."

    # Encontra e compila todos os .java no diretório de origem que NÃO são de teste
    find "$SRC_DIR" -name "*.java" ! -name "*Test.java" | xargs javac -g
fi

# 3. Verifica o resultado da compilação
if [ $? -eq 0 ]; then
    echo "✅ Compilação concluída com sucesso."
else
    echo "❌ A compilação falhou com erros."
fi
