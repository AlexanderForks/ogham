package fr.sii.ogham.sms.builder;

import fr.sii.ogham.core.exception.builder.BuildException;
import fr.sii.ogham.sms.message.addressing.translator.CompositePhoneNumberTranslator;
import fr.sii.ogham.sms.message.addressing.translator.DefaultHandler;
import fr.sii.ogham.sms.message.addressing.translator.PhoneNumberTranslator;

/**
 * Builder to construct the {@link PhoneNumberTranslator} implementation in
 * charge of default addressing policy (TON / NPI).
 * 
 * @author cdejonghe
 * 
 */
public class DefaultPhoneNumberTranslatorBuilder implements PhoneNumberTranslatorBuilder {
	@Override
	public PhoneNumberTranslator build() throws BuildException {
		CompositePhoneNumberTranslator translator = new CompositePhoneNumberTranslator();
		translator.add(new DefaultHandler());
		return translator;
	}

	@Override
	public PhoneNumberTranslatorBuilder useDefaults() {
		return this;
	}

}
