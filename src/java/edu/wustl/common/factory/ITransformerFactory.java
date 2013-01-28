package edu.wustl.common.factory;

import edu.wustl.common.transformer.Transformer;

public interface ITransformerFactory {

	public Transformer getTransformer(int formId);
}
