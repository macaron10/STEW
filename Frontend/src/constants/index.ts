export const API_BASE_URL = 'http://localhost:8399/api';
export const ACCESS_TOKEN_STRING = 'accessToken';
export const REFRESH_TOKEN_STRING = 'refreshToken';
export const TOKEN_PREFIX = 'Bearer ';

// + Provider
export const OAUTH2_REDIRECT_URI = API_BASE_URL + '/login/oauth2/code/'

export const GOOGLE_AUTH_URL = API_BASE_URL + '/oauth2/authorize/google?redirect_uri=' + OAUTH2_REDIRECT_URI;
export const FACEBOOK_AUTH_URL = API_BASE_URL + '/oauth2/authorize/facebook?redirect_uri=' + OAUTH2_REDIRECT_URI;
export const GITHUB_AUTH_URL = API_BASE_URL + '/oauth2/authorize/github?redirect_uri=' + OAUTH2_REDIRECT_URI;
export const CUSTOM_AUTH_URL = API_BASE_URL + '/oauth2/authorization/';

// logo 다운 받아서 불러오게
export const KAKAO_LOGO_URL = "https://developers.kakao.com/tool/resource/static/img/button/kakaolink/kakaolink_btn_medium.png";
export const NAVER_LOGO_URL = "https://encrypted-tbn0.gstatic.com/images?q=tbn%3AANd9GcQsbSxmIq_pUpdRwLIgNmmRJqM6o7GumRr5tWDSZn4&usqp=CAU";
export const GOOGLE_LOGO_URL = "https://developers.google.com/identity/images/g-logo.png?hl=ko";
export const FACEBOOK_LOGO_URL = "https://facebookbrand.com/wp-content/uploads/2019/04/f_logo_RGB-Hex-Blue_512.png?w=512&h=512";


export function getCustomAuthUrl(provider: string){
    return CUSTOM_AUTH_URL + provider;
}