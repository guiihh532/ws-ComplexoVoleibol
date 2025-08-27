<template>
  <div class="homepage">
    <h1>Complexo Voleibol</h1>
  </div>
</template>

<script>
export default {
  name: 'HomePage'
}
</script>

<style scoped>
.homepage {
  /* Garante que a div ocupe a altura total da tela */
  min-height: 100vh;
  /* Centraliza o conteúdo (o h1) usando flexbox */
  display: flex;
  justify-content: center;
  align-items: center;
  /* Deixa o fundo neutro, pronto para ser um "home" em segundo plano */
  background-color: #f0f2f5; 
}

h1 {
  /* Aplica o negrito e estilo ao título */
  font-weight: bold;
  font-size: 3em;
  color: #333;
  text-shadow: 2px 2px 4px rgba(0, 0, 0, 0.1);
  /* Centraliza o texto, caso ocupe mais de uma linha */
  text-align: center; 
}
</style>