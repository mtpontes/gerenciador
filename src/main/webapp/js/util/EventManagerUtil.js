// Atribui funções de evento a elementos
export class EventManagerUtil {
	constructor(){
		this.associatesCollection = [];
	}
	
	// Associa elemento alvo X função de evento
	associateTargetAndEvent(className, eventFunction){
		this.associatesCollection.push({className: className, eventFunction: eventFunction});
		return this;
	}
	
	// Atribui o evento ao elemento alvo
	assignEventsToTarget(fatherElement) {
		this.associatesCollection.forEach(objeto => {
			objeto.eventFunction(fatherElement.querySelectorAll(objeto.className));
		});
	}
}