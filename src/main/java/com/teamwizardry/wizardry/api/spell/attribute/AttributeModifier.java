package com.teamwizardry.wizardry.api.spell.attribute;

import com.teamwizardry.wizardry.api.spell.attribute.AttributeRegistry.Attribute;

public class AttributeModifier {

	private AttributeRegistry.Attribute attribute;
	private double modifier;
	private Operation op;

	public AttributeModifier(Attribute attribute, double modifier, Operation op) {
		this.attribute = attribute;
		this.modifier = modifier;
		this.op = op;
	}

	public double apply(double currentValue) {
		return op.apply(currentValue, modifier);
	}

	public Attribute getAttribute() {
		return attribute;
	}

	public Operation getOperation() {
		return op;
	}

	public double getModifier() {
		return modifier;
	}

	public void setModifier(double newValue) {
		modifier = newValue;
	}

	public AttributeModifier copy() {
		return new AttributeModifier(attribute, modifier, op);
	}

	@Override
	public String toString() {
		return attribute.getShortName() + ": " + op + " " + modifier;
	}
}
