import { MergerSettingsType } from '@ant-design/pro-layout/es/components/SettingDrawer';
import type { ProSettings } from '@ant-design/pro-layout/es/defaultSettings';

/**
 * @name
 */
const Settings: MergerSettingsType<ProSettings> & {
  pwa?: boolean;
  logo?: string;
} = {
  navTheme: 'light',
  layout: 'mix',
  contentWidth: 'Fluid',
  fixedHeader: false,
  fixSiderbar: true,
  colorPrimary: '#13C2C2',
  splitMenus: false,
  siderMenuType: 'sub',

  colorWeak: false,
  // 设置标题的 title
  title: 'Samoyer API接口平台',
  pwa: true,
  // 修改左上角的 logo
  logo: '/icons/logo.svg',
  iconfontUrl: '',
  // token: {
  //   // 参见ts声明，demo 见文档，通过token 修改样式
  //   //https://procomponents.ant.design/components/layout#%E9%80%9A%E8%BF%87-token-%E4%BF%AE%E6%94%B9%E6%A0%B7%E5%BC%8F
  // },
};

export default Settings;
