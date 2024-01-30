/**
 * Padrões de expressões regulares para validação de dados relacionados a empresas.
 * @constant
 * @type {Object}
 */
export const empresaRegexPatterns = {
    /**
     * Padrão para validação de nomes de empresas.
     * @type {RegExp}
     */
    NOME_EMPRESA_PATTERN: new RegExp(/^[\p{L}0-9\s.'-]{1,100}$/u),

    /**
     * Padrão para validação de datas de empresas no formato "dd/mm/yyyy".
     * @type {RegExp}
     */
    DATA_EMPRESA_PATTERN: new RegExp(/^(0[1-9]|[12][0-9]|3[01])\/(0[1-9]|1[0-2])\/\d{4}$/),
};