import Vue from "vue";
import App from "./App.vue";

import router from "./router/index";

// Bootstrap
import BootstrapVue from "bootstrap-vue";
import "bootstrap-vue/dist/bootstrap-vue.css";
import "bootstrap/dist/css/bootstrap.css";

Vue.use(BootstrapVue);

Vue.config.productionTip = false;

new Vue({
    render: (h) => h(App),
    router,
}).$mount("#app");
