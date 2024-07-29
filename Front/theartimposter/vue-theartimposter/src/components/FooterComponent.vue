<template>
    <footer class="footer">
        <div class="footer-content">
            <div class="project-logo">
                <img src="@/assets/game/image/p-logo.png" alt="Project Logo" />
            </div>
            <div class="description">
                <p>
                    <strong>The Art Imposter</strong>는<br />
                    차례대로 순서를 진행하여 주제를 모르는<br />
                    가짜 예술가를 찾아내는 게임입니다.<br />
                    남들을 기만하고 정치질하여 살아남으세요!
                </p>
            </div>
            <div>
                <div class="team-info">
                    <img src="@/assets/game/image/t-logo.png" alt="Team Logo" class="team-logo" />
                </div>
                <div class="links">
                    <div class="link-item">
                        <img src="@/assets/source/image/discode.png" alt="Discord" class="icon" />
                        <a href="https://discord.gg/your-discord-link" target="_blank">우리 게임 디스코드</a>
                    </div>
                    <div class="link-item">
                        <img src="@/assets/source/image/instar.png" alt="Homepage" class="icon" />
                        <a href="https://www.ourteamhomepage.com" target="_blank">우리 팀의 인별그램</a>
                    </div>
                </div>
            </div>

            <div class="team-description">
                <p><strong>= 팀 소개 =</strong></p>
                <br />
                <p>강훈<br />김창환<br />백재우<br />장희선</p>
            </div>
        </div>
    </footer>
</template>

<script>
export default {};
</script>

<style scoped>
.footer {
    width: 100%;
    background-color: #ebebeb;
    color: #fff;
    display: flex;
    align-items: center;
    justify-content: center;
    border-top: 1px solid #444;
    padding: 20px 0;
}

.footer-content {
    display: flex;
    width: 1280px;
    justify-content: space-between;
    align-items: center;
}

.project-logo img {
    height: 80px;
    margin-left: 100px;
    margin-right: 120px;
    margin-top: 15px;
    scale: 2.5;
}

.description {
    flex: 1;
    margin: 0 20px;
    text-align: left;
    font-size: 16px;
    color: #444;
    text-align: center;
}

.description p {
    margin: 5px 0;
    line-height: 1.5;
}

.links {
    display: flex;
    flex-direction: column;
    align-items: center;
    margin-left: 75px;
    margin-right: 120px;
}

.link-item {
    display: flex;
    align-items: center;
    margin-bottom: 10px;
}

.link-item a {
    color: #1e90ff;
    text-decoration: none;
    margin-left: 10px;
    transition: color 0.3s ease;
}

.link-item a:hover {
    color: #63a4ff;
}

.icon {
    height: 24px;
}

.team-info {
    display: flex;
    flex-direction: column;
    align-items: center;
    text-align: center;
}

.team-info img {
    height: 60px;
    margin-right: 50px;
    margin-top: 15px;
    margin-bottom: 10px;
    scale: 1.5;
}

.team-description {
    margin-top: 5px;
}

.team-description p {
    margin: 0;
    font-size: 12px;
    line-height: 1.5;
    color: #444;
    margin-right: 100px;
    scale: 1.25;
}
</style>
