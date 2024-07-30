import Vue from 'vue';
import App from './App.vue';
import router from './router/index';
import store from '@/store/store';
import socketPlugin from './plugins/socket';

// Bootstrap
import BootstrapVue from 'bootstrap-vue';
import 'bootstrap-vue/dist/bootstrap-vue.css';
import 'bootstrap/dist/css/bootstrap.css';

Vue.use(BootstrapVue);

Vue.config.productionTip = false;

Vue.use(socketPlugin, { store }); // 여기서 store를 전달

new Vue({
    router,
    store,
    render: (h) => h(App),
}).$mount('#app');
