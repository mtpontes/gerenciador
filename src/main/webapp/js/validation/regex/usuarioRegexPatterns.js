/**
 * Padrões de expressões regulares para validação de dados relacionados a usuários.
 *
 * @constant
 * @type {Object}
 */
export const usuarioRegexPatterns = {
    /**
     * Padrão para validação de nomes de usuário.
     * @type {RegExp}
     */
    NOME_USUARIO_PATTERN: new RegExp(/^[A-Za-zÀ-ÖØ-öø-ÿ' _-]+$/),

    /**
     * Padrão para validação de logins de usuário.
     * @type {RegExp}
     */
    LOGIN_USUARIO_PATTERN: new RegExp(/^.{3,20}$/),

    /**
     * Padrão para validação de senhas de usuário.
     * @type {RegExp}
     */
    SENHA_USUARIO_PATTERN: new RegExp(/.{8,}/),

    /**
     * Padrão para validação de confirmação de senha de usuário.
     * @type {RegExp}
     */
    CONFIRMA_SENHA_USUARIO_PATTERN: new RegExp(/.+/),
};