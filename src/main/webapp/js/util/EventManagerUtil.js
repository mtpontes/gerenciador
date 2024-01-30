/**
 * Utilitário para atribuição de funções de evento a elementos.
 *
 * @class EventManagerUtil
 */
export class EventManagerUtil {
    constructor() {
        this.associatesCollection = [];
    }

    /**
     * Associa um elemento alvo a uma função de evento.
     *
     * @param {string} className - Nome da classe do elemento alvo.
     * @param {Function} eventFunction - Função de evento a ser associada.
     * @returns {EventManagerUtil} - Própria instância do EventManagerUtil para encadeamento.
     */
    associateTargetAndEvent(className, eventFunction) {
        this.associatesCollection.push({ className: className, eventFunction: eventFunction });
        return this;
    }

    /**
     * Atribui os eventos aos elementos alvo.
     *
     * @param {HTMLElement} fatherElement - Elemento pai alvo.
     */
    assignEventsToTarget(fatherElement) {
        this.associatesCollection.forEach(objeto => {
            objeto.eventFunction(fatherElement.querySelectorAll(objeto.className));
        });
    }
}
