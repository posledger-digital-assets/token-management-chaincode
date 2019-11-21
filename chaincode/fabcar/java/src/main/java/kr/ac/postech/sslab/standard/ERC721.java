package kr.ac.postech.sslab.standard;

import kr.ac.postech.sslab.main.ConcreteChaincodeBase;
import org.hyperledger.fabric.shim.ChaincodeStub;
import org.hyperledger.fabric.shim.ledger.KeyValue;
import org.hyperledger.fabric.shim.ledger.QueryResultsIterator;
import java.util.ArrayList;
import java.util.List;

public class ERC721 extends ConcreteChaincodeBase implements IERC721 {
	private BaseNFT baseNFT = new BaseNFT();

	@Override
	public Response balanceOf(ChaincodeStub stub, List<String> args) {
		try {
			if (args.size() != 1) {
				throw new Throwable("FAILURE");
			}

			String owner = args.get(0).toLowerCase();
			long ownedTokensCount = this.getBalance(stub, owner);

			return newSuccessResponse(Long.toString(ownedTokensCount));
		} catch (Throwable throwable) {
			return newErrorResponse("FAILURE");
		}
	}

	private long getBalance(ChaincodeStub stub, String owner) {
		String query = "{\"selector\":{\"owner\":\"" + owner + "\"}}";

		long ownedTokensCount = 0;
		QueryResultsIterator<KeyValue> resultsIterator = stub.getQueryResult(query);
		while(resultsIterator.iterator().hasNext()) {
			resultsIterator.iterator().next();
			ownedTokensCount++;
		}

		return ownedTokensCount;
	}

	@Override
	public Response ownerOf(ChaincodeStub stub, List<String> args) {
		return this.baseNFT.getOwner(stub, args);
	}

	@Override
	public Response transferFrom(ChaincodeStub stub, List<String> args) {
		return this.baseNFT.setOwner(stub, args);
	}

	@Override
	public Response approve(ChaincodeStub stub, List<String> args) {
		return this.baseNFT.setApprovee(stub, args);
	}
	
	@Override
	public Response setApprovalForAll(ChaincodeStub stub, List<String> args) {
		return this.baseNFT.setOperatorForCaller(stub, args);
	}

	@Override
    public Response getApproved(ChaincodeStub stub, List<String> args) {
		return this.baseNFT.getApprovee(stub, args);
	}

	@Override
	public Response isApprovedForAll(ChaincodeStub stub, List<String> args) {
		return this.baseNFT.isOperatorForCaller(stub, args);
	}

	public Response mint(ChaincodeStub stub, List<String> args) {
		try {
			if (args.size() != 2) {
				throw new Throwable("FAILURE");
			}

			String id = args.get(0).toLowerCase();
			String type = "erc721";
			String owner = args.get(1).toLowerCase();

			List<String> newArgs = new ArrayList<>();
			newArgs.add(id);
			newArgs.add(type);
			newArgs.add(owner);

			return this.baseNFT.mint(stub, newArgs);
		} catch (Throwable throwable) {
			return newErrorResponse("FAILURE");
		}
	}
}