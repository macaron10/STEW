// export const API_BASE_URL = 'http://localhost:8399/api';
export const API_BASE_URL = 'https://i3b103.p.ssafy.io/api'
export const ACCESS_TOKEN_STRING = 'accessToken';
export const REFRESH_TOKEN_STRING = 'refreshToken';
export const TOKEN_PREFIX = 'Bearer ';

// + Provider
export const OAUTH2_REDIRECT_URI = API_BASE_URL + '/login/oauth2/code'

export const GOOGLE_AUTH_URL = API_BASE_URL + '/oauth2/authorize/google?redirect_uri=' + OAUTH2_REDIRECT_URI;
export const FACEBOOK_AUTH_URL = API_BASE_URL + '/oauth2/authorize/facebook?redirect_uri=' + OAUTH2_REDIRECT_URI;
export const GITHUB_AUTH_URL = API_BASE_URL + '/oauth2/authorize/github?redirect_uri=' + OAUTH2_REDIRECT_URI;
export const CUSTOM_AUTH_URL = API_BASE_URL + '/oauth2/authorization/';

export const KAKAO_LOGO_URL = "/image/social\\kakao_normal.png";
export const NAVER_LOGO_URL = "/image/social\\naver_normal.png";
export const GOOGLE_LOGO_URL = "/image/social\\google_normal.png";
export const FACEBOOK_LOGO_URL = "/image/social\\facebook_normal.png";


export function getCustomAuthUrl(provider: string){
    return CUSTOM_AUTH_URL + provider;
}