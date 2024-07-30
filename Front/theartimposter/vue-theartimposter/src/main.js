<<<<<<< HEAD
import Vue from 'vue';
import App from './App.vue';
import store from './store';
import router from './router';
import socketPlugin from './plugins/socket';
=======
import Vue from "vue";
import App from "./App.vue";
import router from "./router/index";
import store from "@/store/store";
>>>>>>> fc8ead5b748d10470f748dee1bfa49abaa6b668e

// Bootstrap
import BootstrapVue from "bootstrap-vue";
import "bootstrap-vue/dist/bootstrap-vue.css";
import "bootstrap/dist/css/bootstrap.css";

Vue.use(BootstrapVue);

Vue.config.productionTip = false;

Vue.use(socketPlugin, { store }); // 여기서 store를 전달

new Vue({
<<<<<<< HEAD
    store,
    router,
    render: (h) => h(App),
}).$mount('#app');
=======
    router,
    store,
    render: (h) => h(App),
}).$mount("#app");
>>>>>>> fc8ead5b748d10470f748dee1bfa49abaa6b668e
