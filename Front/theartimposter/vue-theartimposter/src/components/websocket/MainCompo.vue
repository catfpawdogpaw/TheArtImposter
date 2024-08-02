<template>
  <div class="main-container">
    <h1>가짜 예술가 찾기</h1>
    <h2>예술적 감각을 시험해 보세요!</h2>
    <p>우리 웹사이트는 당신의 예술 감각을 시험할 수 있는 흥미진진한 게임을 제공합니다. 진짜 예술작품과 가짜 예술작품을 구분할 수 있는 눈을 가지고 계신가요? 도전해보세요!</p>
    <h2>게임 방법</h2>
    <ol>
      <li>각 라운드마다 여러 개의 예술작품이 제시됩니다.</li>
      <li>그 중에서 가짜 예술작품을 찾아내면 됩니다.</li>
      <li>각 예술작품에 대한 설명과 힌트를 참고하여 진짜와 가짜를 구분하세요.</li>
    </ol>
    <h2>특징</h2>
    <ul>
      <li>다양한 예술작품: 전 세계의 다양한 예술작품을 경험할 수 있습니다.</li>
      <li>레벨별 난이도: 초보자부터 전문가까지, 모든 수준의 플레이어를 위한 다양한 난이도가 준비되어 있습니다.</li>
      <li>순위 경쟁: 다른 플레이어들과 경쟁하여 최고 점수를 기록해 보세요.</li>
    </ul>
    <p>지금 바로 게임을 시작하고 예술적 통찰력을 증명해보세요!</p>
    <div class="btn-container">
      <a href="#" class="btn">게임 시작</a>
    </div>
  </div>
</template>

<script>
  export default {

  }
</script>

<style scoped>
.main-container {
  border: 1px solid white;
  border-radius: 10%;
  min-width: 500px;
  min-height: 500px;
}
.main-container {
  max-width: 800px;
  margin: 50px auto;
  padding: 20px;
  background-color: #273c75;
  box-shadow: 0 0 10px rgba(0, 0, 0, 0.1);
  border-radius: 8px;
}
h1 {
  color: #fff;
  text-align: center;
}
h2 {
  color: #fff;
}
p {
  color: #fff;
}
ol, ul {
  margin: 0;
  padding: 0;
  list-style-position: inside;
}
ol li, ul li {
  margin-bottom: 10px; /* 간격 조정 */
  padding-left: 20px;
}
.btn-container {
  text-align: center;
  margin-top: 20px;
}
.btn {
  background-color: #fbc531; /* 밝은 노란색 */
  color: #273c75; /* 배경 색상과 일치하는 텍스트 색상 */
  padding: 10px 20px;
  border: none;
  border-radius: 5px;
  cursor: pointer;
  text-decoration: none;
  font-size: 16px;
  transition: background-color 0.3s, transform 0.2s;
}
.btn:hover {
  background-color: #e1b12c; /* 호버 상태에서는 어두운 노란색 */
  transform: scale(1.05); /* 약간 확대 */
}
.btn:active {
  background-color: #d4a517; /* 클릭 시 어두운 노란색 */
  transform: scale(0.95); /* 약간 축소 */
}
</style>
